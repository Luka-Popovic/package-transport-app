/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl140170;

/**
 *
 * @author Luka
 */

import operations.*;
import student.*;
import tests.TestRunner;


public class Main {

    
    public static void main(String[] args) {
        CityOperations cityOperations = new pl140170_CityOperations(); // Change this to your implementation.
        DistrictOperations districtOperations = new pl140170_DistrictOperations(); // Do it for all classes.
        CourierOperations courierOperations = new pl140170_CourierOperations(); // e.g. = new MyDistrictOperations();
        CourierRequestOperation courierRequestOperation = new pl140170_CourierRequestOperation();
        GeneralOperations generalOperations = new pl140170_GeneralOperations();
        UserOperations userOperations = new pl140170_UserOperations();
        VehicleOperations vehicleOperations = new pl140170_VehicleOperations();
        PackageOperations packageOperations = new pl140170_PackageOperations();

        
        tests.TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                packageOperations);

           TestRunner.runTests();
         
    }
}
