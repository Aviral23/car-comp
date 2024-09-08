package com.intuit.validator;

import com.intuit.exception.ValidationException;
import com.intuit.models.Car;
import com.intuit.request.CompareRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestValidator {
    public void validateCompareRequest(CompareRequest compareRequest) {
        if(compareRequest == null) {
            throw new ValidationException("id list is empty");
        } else if (compareRequest.getIdList().size() > 2) {
            throw new ValidationException("id list has size greater/lesser than required");
        }
        else if (compareRequest.getIdList().contains(compareRequest.getPresentCarId())) {
            throw new ValidationException("present car id and comparison id is same ");
        }

    }

    public void validateIfCarsExist(List<Car> cars) {
        if(cars == null) {
            throw new ValidationException("No cars found for this particular search query");
        }
    }
}
