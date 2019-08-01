package alararestaurant.service;

import alararestaurant.common.FilePaths;
import alararestaurant.common.OutputMessages;
import alararestaurant.domain.dtos.employee.ImportEmployeeJson;
import alararestaurant.domain.entities.Employee;
import alararestaurant.domain.entities.Position;
import alararestaurant.repository.EmployeeRepository;
import alararestaurant.repository.PositionRepository;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final Gson gson;
    private final FileUtil fileUtil;
    private final ModelMapper modelMapper;
    private final ValidationUtil validator;
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;

    @Autowired
    public EmployeeServiceImpl(Gson gson, FileUtil fileUtil, ModelMapper modelMapper, ValidationUtil validator, EmployeeRepository employeeRepository, PositionRepository positionRepository) {
        this.gson = gson;
        this.fileUtil = fileUtil;
        this.modelMapper = modelMapper;
        this.validator = validator;
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
    }

    @Override
    public Boolean employeesAreImported() {
        return this.employeeRepository.count() > 0;
    }

    @Override
    public String readEmployeesJsonFile() throws IOException {
        return fileUtil.readFile(FilePaths.EMPLOYEE_JSON_PATH);
    }

    @Override
    public String importEmployees(String employees) {
        StringBuilder importInfo = new StringBuilder();

        ImportEmployeeJson[] allEmployees = gson.fromJson(employees, ImportEmployeeJson[].class);
        for (ImportEmployeeJson employeeJson : allEmployees) {
            if (! validator.isValid(employeeJson)) {
                importInfo.append(OutputMessages.INVALID_DATA_FOR_ENTITY);
                continue;
            }

            Position position = this.addPositionIfMissing(employeeJson.getPosition());

            Employee employee = modelMapper.map(employeeJson, Employee.class);

            employee.setPosition(position);

            employeeRepository.saveAndFlush(employee);

            importInfo.append(String.format(OutputMessages.SUCCESSFULLY_IMPORT_ENTITY, employee.getName()));
        }
        return importInfo.toString();
    }

    private Position addPositionIfMissing(String positionName) {
        Position position = positionRepository.findByName(positionName).orElse(null);

        if (position == null) {
            position = new Position();

            position.setName(positionName);

            positionRepository.saveAndFlush(position);
        }

        return position;
    }
}
