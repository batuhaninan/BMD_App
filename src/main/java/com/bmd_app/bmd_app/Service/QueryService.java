package com.bmd_app.bmd_app.Service;

import com.bmd_app.bmd_app.Entity.Delivery;
import com.bmd_app.bmd_app.Repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

@Service
public class QueryService {

    @Autowired
    DeliveryRepository deliveryRepository;

    public Hashtable<Long, Integer> requestCount(Date startTime, Date endTime){
        ArrayList<Delivery> deliveries = (ArrayList<Delivery>) deliveryRepository.findAll();
        Hashtable<Long, Integer> requestsByClient = new Hashtable<Long, Integer>();
        for (Delivery delivery : deliveries){
            if (delivery.getResultCode() == -1){
                continue;
            }
            if (delivery.getRequest().getStartTime().after(startTime)){
                if (endTime.after(delivery.getRequest().getEndTime())){
                    if (requestsByClient.get(delivery.getRequest().getClient().getId())!=null){
                        requestsByClient.put(delivery.getRequest().getClient().getId(), requestsByClient.get(delivery.getRequest().getClient().getId())+1);
                    }
                    else{
                        requestsByClient.put(delivery.getRequest().getClient().getId(),1);
                    }
                }
            }
        }
        return requestsByClient;
    }


    public Hashtable<String,Integer> requestStats(Date startTime, Date endTime){
        Hashtable<String, Integer> stats = new Hashtable<String, Integer>();
        stats.put("success",0);
        stats.put("failed",0);
        ArrayList<Delivery> deliveries = (ArrayList<Delivery>) deliveryRepository.findAll();
        for (Delivery delivery : deliveries){
            if (delivery.getResultCode() == -1){
                continue;
            }
            if (delivery.getRequest().getStartTime().after(startTime)){
                if (endTime.after(delivery.getRequest().getEndTime())){
                    if (delivery.getResultCode() == 0 || delivery.getResultCode() == 200 ){
                        stats.put("success",stats.get("success")+1);
                    }
                    else{
                        stats.put("failed",stats.get("failed")+1);
                    }
                }
            }
        }
        return stats;
    }


    public Hashtable<Long, Integer> requestErrorCount(Date startTime, Date endTime){
        Hashtable<Long, Integer> errors = new Hashtable<Long, Integer>();
        ArrayList<Delivery> deliveries = (ArrayList<Delivery>) deliveryRepository.findAll();
        for (Delivery delivery : deliveries){
            if (delivery.getResultCode() == -1){
                continue;
            }
            if (delivery.getRequest().getStartTime().after(startTime)){
                if (endTime.after(delivery.getRequest().getEndTime())){
                    if(delivery.getResultCode()!=0){ // fix request.getSuccess
//						if (Long.valueOf(delivery.getResultCode()).equals(null)){
//							continue;
//						}
                        if (errors.get(delivery.getResultCode()) != null) {
                            errors.put(delivery.getResultCode(), errors.get(delivery.getResultCode()) + 1);
                        }
                        else{
                            errors.put(delivery.getResultCode(), 1);
                        }
                    }
                }
            }
        }
        return errors;
    }

    public Hashtable<Long,Hashtable<Long, Integer>> RequestErrorCountsPerClient(Date startTime, Date endTime){
        Hashtable<Long,Hashtable<Long, Integer>> errors = new Hashtable<Long, Hashtable<Long, Integer>>();
        ArrayList<Delivery> deliveries = (ArrayList<Delivery>) deliveryRepository.findAll();
        for (Delivery delivery : deliveries){
            if (delivery.getResultCode() == -1){
                continue;
            }
            if (delivery.getRequest().getStartTime().after(startTime)){
                if (endTime.after(delivery.getRequest().getEndTime())){
                    if(delivery.getResultCode()!=0){
                        Hashtable<Long, Integer> innerMap = new Hashtable<Long, Integer>();
                        if(errors.get(delivery.getRequest().getClient().getId())!=null){
                            if ( errors.get( delivery.getRequest().getClient().getId() ).get( delivery.getResultCode() ) != null ){
                                innerMap = errors.get(delivery.getRequest().getClient().getId());
                                innerMap.put(delivery.getResultCode(),errors.get(delivery.getRequest().getClient().getId()).get(delivery.getResultCode())+1);
                                errors.put(delivery.getRequest().getClient().getId(), innerMap);
                            }
                            else{
                                innerMap = errors.get(delivery.getRequest().getClient().getId());
                                innerMap.put(delivery.getResultCode(), 1);
                                errors.put(delivery.getRequest().getClient().getId(), innerMap);
                            }
                        }
                        else{
                            innerMap.put(delivery.getResultCode(), 1);
                            errors.put(delivery.getRequest().getClient().getId(), innerMap);
                        }
                    }
                }
            }
        }
        return errors;
    }

}
