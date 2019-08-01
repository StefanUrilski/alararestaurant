package alararestaurant.service;

import alararestaurant.common.FilePaths;
import alararestaurant.common.OutputMessages;
import alararestaurant.domain.dtos.order.ImportOrderNestedItemRootXml;
import alararestaurant.domain.dtos.order.ImportOrderNestedItemXml;
import alararestaurant.domain.dtos.order.ImportOrderRootXml;
import alararestaurant.domain.dtos.order.InputOrderXml;
import alararestaurant.domain.entities.*;
import alararestaurant.repository.*;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {
    private static final String NEW_LINE = System.lineSeparator();

    private final FileUtil fileUtil;
    private final ModelMapper modelMapper;
    private final ValidationUtil validator;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final EmployeeRepository employeeRepository;
    private final OrderItemRepository orderItemsRepository;

    @Autowired
    public OrderServiceImpl(FileUtil fileUtil,
                            ModelMapper modelMapper,
                            ValidationUtil validator,
                            ItemRepository itemRepository,
                            OrderRepository orderRepository,
                            EmployeeRepository employeeRepository,
                            OrderItemRepository orderItemsRepository) {
        this.fileUtil = fileUtil;
        this.modelMapper = modelMapper;
        this.validator = validator;
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
        this.employeeRepository = employeeRepository;
        this.orderItemsRepository = orderItemsRepository;
    }

    @Override
    public Boolean ordersAreImported() {
        return this.orderRepository.count() > 0;
    }

    @Override
    public String readOrdersXmlFile() throws IOException {
        return fileUtil.readFile(FilePaths.ORDERS_XML_PATH);
    }

    @Override
    public String importOrders() throws JAXBException {
        StringBuilder importInfo = new StringBuilder();
        JAXBContext jaxbContext = JAXBContext.newInstance(ImportOrderRootXml.class);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        ImportOrderRootXml orders = (ImportOrderRootXml) unmarshaller.unmarshal(new File(FilePaths.ORDERS_XML_PATH));

        TypeMap<InputOrderXml, Order> typeMap =
                modelMapper.createTypeMap(InputOrderXml.class, Order.class);

        typeMap.addMappings(ctx -> ctx.using(dateTime())
                .map(source -> source, Order::setDateTime));

        typeMap.addMappings(ctx -> ctx.using(type())
                .map(source -> source, Order::setType));

        for (InputOrderXml orderXml : orders.getOrder()) {
            if (!validator.isValid(orderXml)) {
                continue;
            }

            Employee employee = getEmployee(orderXml.getEmployee());

            if (employee == null) {
                continue;
            }

            Set<OrderItem> orderItems = getAllItems(orderXml.getItems());

            if (orderItems == null) {
                continue;
            }

            Order order = modelMapper.map(orderXml, Order.class);

            order.setEmployee(employee);
            order.setOrderItems(orderItems);

            orderRepository.saveAndFlush(order);

            for (OrderItem item : orderItems) {
                item.setOrder(order);
                orderItemsRepository.saveAndFlush(item);
            }

            importInfo.append(String.format(OutputMessages.SUCCESSFULLY_IMPORT_ORDER,
                    orderXml.getEmployee(),
                    orderXml.getDateTime()
            ));
        }

        return importInfo.toString();
    }

    private Employee getEmployee(String employeeName) {
        return employeeRepository.findByName(employeeName).orElse(null);
    }

    private Set<OrderItem> getAllItems(ImportOrderNestedItemRootXml items) {
        Set<OrderItem> allItems = new HashSet<>();
        for (ImportOrderNestedItemXml currItem : items.getItems()) {
            Item item = itemRepository.findByName(currItem.getName()).orElse(null);

            if (item == null) {
                return null;
            }
            OrderItem orderItem = new OrderItem();

            orderItem.setItem(item);
            orderItem.setQuantity(currItem.getQuantity());
            allItems.add(orderItem);
        }

        return allItems;
    }

    private Converter<InputOrderXml, LocalDateTime> dateTime() {
        return ctx -> {
            String date = ctx.getSource().getDateTime();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            return LocalDateTime.parse(date, formatter);
        };
    }

    private Converter<InputOrderXml, Type> type() {
        return ctx -> Type.valueOf(ctx.getSource().getType());
    }

    @Override
    @Transactional
    public String exportOrdersFinishedByTheBurgerFlippers() {
        StringBuilder finishedOrders = new StringBuilder();
        List<Order> ordersBurgerFlippers = orderRepository.findAllByEmployeePosition("Burger Flipper");

        ordersBurgerFlippers.forEach(order -> {
            finishedOrders.append("Name: ").append(order.getEmployee().getName()).append(NEW_LINE);
            finishedOrders.append("Order:").append(NEW_LINE);
            for (Order orderByEmployee : order.getEmployee().getOrders()) {
                finishedOrders.append(" Customer: ").append(orderByEmployee.getCustomer()).append(NEW_LINE);
                finishedOrders.append(" Items:").append(NEW_LINE);
                for (OrderItem orderItem : orderByEmployee.getOrderItems()) {
                    finishedOrders.append("  Name: ").append(orderItem.getItem().getName()).append(NEW_LINE);
                    finishedOrders.append("  Price: ").append(orderItem.getItem().getPrice()).append(NEW_LINE);
                    finishedOrders.append("  Quantity: ").append(orderItem.getQuantity()).append(NEW_LINE);
                }
            }
        });
        return finishedOrders.toString();
    }

}
