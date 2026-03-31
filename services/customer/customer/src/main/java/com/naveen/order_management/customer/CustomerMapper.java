package com.naveen.order_management.customer;

import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toCustomer(CustomerRequest request) {
        if (request == null) {
            return null;
        }

        //use builder later
        Customer customer = new Customer();
        customer.setId(request.id());
        customer.setFirstname(request.firstname());
        customer.setLastname(request.lastname());
        customer.setEmail(request.email());
        customer.setAddress(request.address());
        return customer;
    }

    public CustomerResponse fromCustomer(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerResponse(
                customer.id,
                customer.firstname,
                customer.lastname,
                customer.email,
                customer.address
        );
    }
}