package com.solvd.delivery_service.persistence;

import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.pack.Package;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface PackageRepository {
    void create(@Param("pack") Package pack);
    Optional<Package> findById(@Param("id") Long id);
    List<Package> findAll();
    void update(@Param("pack") Package pack, @Param("field") String field);
    void deleteById(@Param("id") Long id);
    Long countOfEntries();
    Long findMaxPackageNumber();
    List<Package> findCustomerPackages(@Param("customer") Customer customer);
    List<Package> findEmployeePackages(@Param("employee") Employee employee);
}