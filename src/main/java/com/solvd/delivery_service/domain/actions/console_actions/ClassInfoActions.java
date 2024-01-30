package com.solvd.delivery_service.domain.actions.console_actions;

import com.solvd.delivery_service.domain.actions.Actions;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.util.console_menu.RequestMethods;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.solvd.delivery_service.util.Printers.*;

public class ClassInfoActions extends Actions {

    public static Field getAnyClassFieldFromConsole(Class<?> clazz) {
        int index = 1;
        PRINTLN.info("Choose the field:");
        List<Field> allFields = List.of(clazz.getDeclaredFields());
        List<Field> fields = new ArrayList<>();
        for (Field item : allFields) {
            if (!item.getName().equals("id")) {
                printAsMenu.print(index, item.getName());
                fields.add(item);
                index++;
            }
        }
        return fields.get(RequestMethods.getNumberFromChoice("field number", index - 1) - 1);
    }

    public static Field getEmployeeClassFieldFromConsole() {
        int index = 1;
        PRINTLN.info("Choose the field:");
        List<Field> allEmployeeFields = List.of(Employee.class.getDeclaredFields());
        List<Field> employeeFields = new ArrayList<>();
        for (Field employeeField : allEmployeeFields) {
            if (!employeeField.getName().equals("id") && !employeeField.getName().equals("packages")) {
                printAsMenu.print(index, employeeField.getName());
                employeeFields.add(employeeField);
                index++;
            }
        }
        return employeeFields.get(RequestMethods.getNumberFromChoice("field number", index - 1) - 1);
    }

    public static Field getDepartmentClassFieldFromConsole() {
        int index = 1;
        PRINTLN.info("Choose the field:");
        List<Field> allDepartmentFields = List.of(Department.class.getDeclaredFields());
        List<Field> departmentFields = new ArrayList<>();
        for (Field departmentField : allDepartmentFields) {
            if (!departmentField.getName().equals("id") && !departmentField.getName().equals("employees")) {
                printAsMenu.print(index, departmentField.getName());
                departmentFields.add(departmentField);
                index++;
            }
        }
        return departmentFields.get(RequestMethods.getNumberFromChoice("field number", index - 1) - 1);
    }
}