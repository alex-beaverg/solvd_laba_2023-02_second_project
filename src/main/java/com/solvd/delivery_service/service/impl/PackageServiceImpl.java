package com.solvd.delivery_service.service.impl;

import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.pack.Package;
import com.solvd.delivery_service.persistence.PackageRepository;
import com.solvd.delivery_service.service.AddressService;
import com.solvd.delivery_service.service.CustomerService;
import com.solvd.delivery_service.service.EmployeeService;
import com.solvd.delivery_service.util.console_menu.DaoService;
import com.solvd.delivery_service.service.PackageService;

import java.util.List;

public class PackageServiceImpl implements PackageService {
    private static final DaoService DAO_SERVICE = DaoService.getInstance();
    private final PackageRepository packageRepository;
    private final AddressService addressService;
    private final CustomerService customerService;
    private final EmployeeService employeeService;

    public PackageServiceImpl() {
        this.packageRepository = DAO_SERVICE.getRepository(PackageRepository.class);
        this.addressService = new AddressServiceImpl();
        this.customerService = new CustomerServiceImpl();
        this.employeeService = new EmployeeServiceImpl();
    }

    @Override
    public Package create(Package pack) {
        pack.setId(null);
        if (pack.getAddressTo() != null) {
            Address addressTo = addressService.create(pack.getAddressTo());
            pack.setAddressTo(addressTo);
        }
        if (pack.getCustomer() != null) {
            Customer customer = customerService.create(pack.getCustomer());
            pack.setCustomer(customer);
        }
        packageRepository.create(pack);
        return pack;
    }

    @Override
    public Package createWithExistingAddressTo(Package pack) {
        pack.setId(null);
        if (pack.getCustomer() != null) {
            Customer customer = customerService.create(pack.getCustomer());
            pack.setCustomer(customer);
        }
        packageRepository.create(pack);
        return pack;
    }

    @Override
    public Package createWithNewEmployee(Package pack) {
        pack.setId(null);
        if (pack.getAddressFrom() != null) {
            Address addressFrom = addressService.create(pack.getAddressFrom());
            pack.setAddressFrom(addressFrom);
        }
        if (pack.getAddressTo() != null) {
            Address addressTo = addressService.create(pack.getAddressTo());
            pack.setAddressTo(addressTo);
        }
        if (pack.getCustomer() != null) {
            Customer customer = customerService.create(pack.getCustomer());
            pack.setCustomer(customer);
        }
        if (pack.getCustomer() != null) {
            Employee employee = employeeService.create(pack.getEmployee(), pack.getEmployee().getDepartment().getId());
            pack.setEmployee(employee);
        }
        packageRepository.create(pack);
        return pack;
    }

    @Override
    public Package retrieveById(Long id) {
        return packageRepository.findById(id).get();
    }

    @Override
    public Package createWithExistingCustomer(Package pack) {
        pack.setId(null);
        if (pack.getAddressTo() != null) {
            Address addressTo = addressService.create(pack.getAddressTo());
            pack.setAddressTo(addressTo);
        }
        packageRepository.create(pack);
        return pack;
    }

    @Override
    public List<Package> retrieveAll() {
        return packageRepository.findAll();
    }

    @Override
    public Long retrieveMaxPackageNumber() {
        return packageRepository.findMaxPackageNumber();
    }

    @Override
    public List<Package> retrieveCustomerPackages(Customer customer) {
        return packageRepository.findCustomerPackages(customer);
    }

    @Override
    public Long retrieveNumberOfEntries() {
        return packageRepository.countOfEntries();
    }

    @Override
    public void removeById(Long id) {
        packageRepository.deleteById(id);
    }

    @Override
    public void updateField(Package pack, String field) {
        packageRepository.update(pack, field);
    }
}