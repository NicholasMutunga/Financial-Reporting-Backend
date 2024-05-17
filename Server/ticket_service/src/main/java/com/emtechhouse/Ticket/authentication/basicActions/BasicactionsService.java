package com.emtechhouse.Ticket.authentication.basicActions;

import com.emtechhouse.Ticket.Utils.EntityResponse;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.UserRequestContext;
import com.emtechhouse.Ticket.authentication.basicActions.Basicactions;
import com.emtechhouse.Ticket.authentication.models.Privilege;
import com.emtechhouse.Ticket.authentication.workclass.Workclass;
import com.emtechhouse.Ticket.authentication.basicActions.BasicactionsRepo;
import com.emtechhouse.Ticket.authentication.repositories.PrivilegeRepo;
import com.emtechhouse.Ticket.authentication.workclass.WorkclassRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class  BasicactionsService {
    private final BasicactionsRepo basicactionsRepo;
    private final PrivilegeRepo privilegeRepo;
    private final WorkclassRepo workclassRepo;

    public BasicactionsService(BasicactionsRepo basicactionsRepo, PrivilegeRepo privilegeRepo, WorkclassRepo workclassRepo) {
        this.basicactionsRepo = basicactionsRepo;
        this.privilegeRepo = privilegeRepo;
        this.workclassRepo = workclassRepo;
    }
    @Transactional
    public EntityResponse addBasicactions(List<Basicactions> basicactions, Long privilegeId, Long workclassId) {
        try {
            EntityResponse response = new EntityResponse();
            Optional<Workclass> workclass = workclassRepo.findById(workclassId);
            if (workclass.isPresent()){
                Optional<Privilege> privilege = privilegeRepo.findById(privilegeId);
                if(privilege.isPresent()){
                    List<Basicactions> newBasicActions = new ArrayList<>();
                    for (int i = 0; i<basicactions.size(); i++){
                        Basicactions basicactions1 = basicactions.get(i);
                        List<Basicactions> basicaction = basicactionsRepo.findByPrivilegeAndWorkclassAndCode(privilege.get(), workclass.get(),basicactions1.getCode());
                        if (basicaction.size()>0){
                            response.setMessage("Basic Action is already mapped!");
                            response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                            response.setEntity("");
                        }else {
                            basicactions1.setPostedBy("User");
                            basicactions1.setPostedFlag('Y');
                            basicactions1.setPostedTime(new Date());
                            basicactions1.setPrivilege(privilege.get());
                            basicactions1.setWorkclass(workclass.get());
                            newBasicActions.add(basicactions1);
                        }
                    }
                    if (newBasicActions.size()>0){
                        response.setMessage(HttpStatus.CREATED.getReasonPhrase());
                        response.setStatusCode(HttpStatus.CREATED.value());
                        response.setEntity(basicactionsRepo.saveAll(newBasicActions));
                        return response;
                    }else {
                        response.setMessage("All the Basic Action provided are already mapped!");
                        response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                        response.setEntity("");
                        return response;
                    }
                }else{
                    response.setMessage("Privilege Not Found!");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return response;
                }
            }else {
                response.setMessage("Work Class Not Found!");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return response;
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    public EntityResponse findAllBasicactionss(Long privilegeId,Long workclassId) {
        try {
            Optional<Workclass> workclass = workclassRepo.findById(workclassId);
            Optional<Privilege> privilege = privilegeRepo.findById(privilegeId);
            List<Basicactions> basicaction = basicactionsRepo.findByPrivilegeAndWorkclassAndDeletedFlag(privilege.get(),workclass.get(), 'N');
            if (basicaction.size() > 0){
                EntityResponse response = new EntityResponse();
                response.setMessage("Found!");
                response.setStatusCode(HttpStatus.OK.value());
                response.setEntity(basicaction);
                return response;
            }else {
                EntityResponse response = new EntityResponse();
                response.setMessage("Not Found!");
                response.setStatusCode(HttpStatus.OK.value());
                response.setEntity("");
                return response;
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    public EntityResponse findById(Long id){
        try {
            Optional<Basicactions> basicaction = basicactionsRepo.findById(id);
            if (basicaction.isPresent()){
                EntityResponse response = new EntityResponse();
                response.setMessage("Found!");
                response.setStatusCode(HttpStatus.OK.value());
                response.setEntity(basicaction);
                return response;
            }else {
                EntityResponse response = new EntityResponse();
                response.setMessage("Not Found!");
                response.setStatusCode(HttpStatus.OK.value());
                response.setEntity("");
                return response;
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }
    public EntityResponse updateBasicactions(Long id,List<Basicactions> basicactions,Long privilegeId, Long workclassId) {
        try {
            EntityResponse response = new EntityResponse();
            response.setMessage("Updated Successfully!");
            response.setStatusCode(HttpStatus.OK.value());
            response.setEntity("");
            for (int i=0;i<basicactions.size(); i++){
                Optional<Basicactions> basicactions1 = basicactionsRepo.findById(basicactions.get(i).getId());
                if (basicactions1.isPresent()){
                    Basicactions basic = basicactions1.get();
                    basic.setModifiedFlag('Y');
                    basic.setModifiedBy(UserRequestContext.getCurrentUser());
                    basic.setModifiedTime(new Date());
                    basic.setSelected(basicactions.get(i).isSelected());
                    basicactionsRepo.save(basic);
                }else{
                    response.setMessage("Not Found!");
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setEntity("");
                }
                if (i==basicactions.size()){
                    response.setMessage("Updated Successfully!");
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setEntity("");
                }
            }
            return response;
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    public EntityResponse verify(Long id) {
        try {
            Optional<Basicactions> basicaction = basicactionsRepo.findById(id);
            if (basicaction.isPresent()){
                Basicactions basicactions = basicaction.get();
                basicactions.setVerifiedBy(UserRequestContext.getCurrentUser());
                basicactions.setVerifiedFlag('Y');
                basicactions.setVerifiedTime(new Date());
                EntityResponse response = new EntityResponse();
                response.setMessage("Verified Successfully!");
                response.setStatusCode(HttpStatus.OK.value());
                response.setEntity("");
                basicactionsRepo.save(basicactions);
                return response;
            }else {
                EntityResponse response = new EntityResponse();
                response.setMessage("Not Found!");
                response.setStatusCode(HttpStatus.OK.value());
                response.setEntity("");
                return response;
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    public EntityResponse deleteTemporary(Long id) {
        try {
            Optional<Basicactions> basicaction = basicactionsRepo.findById(id);
            if (basicaction.isPresent()){
                Basicactions basicactions = basicaction.get();
                basicactions.setDeletedBy(UserRequestContext.getCurrentUser());
                basicactions.setDeletedFlag('Y');
                basicactions.setDeletedTime(new Date());
                EntityResponse response = new EntityResponse();
                response.setMessage("Deleted Successfully!");
                response.setStatusCode(HttpStatus.OK.value());
                response.setEntity("");
                basicactionsRepo.save(basicactions);
                return response;
            }else {
                EntityResponse response = new EntityResponse();
                response.setMessage("Not Found!");
                response.setStatusCode(HttpStatus.OK.value());
                response.setEntity("");
                return response;
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }
    public EntityResponse restoreId(Long id) {
        try {
            Optional<Basicactions> basicaction = basicactionsRepo.findById(id);
            if (basicaction.isPresent()){
                EntityResponse response = new EntityResponse();
                response.setMessage("Restored Successfully!");
                response.setStatusCode(HttpStatus.OK.value());
                response.setEntity("");
                basicaction.get().setDeletedFlag('N');
                basicactionsRepo.save(basicaction.get());
                return response;
            }else {
                EntityResponse response = new EntityResponse();
                response.setMessage("Not Found!");
                response.setStatusCode(HttpStatus.OK.value());
                response.setEntity("");
                return response;
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }
    public EntityResponse deletePermanently(Long privilegeId, Long workclassId) {
        try {
            EntityResponse response = new EntityResponse();
            response.setMessage("Deleted Successfully!");
            response.setStatusCode(HttpStatus.OK.value());
            response.setEntity("");
            Optional<Workclass> workclass = workclassRepo.findById(workclassId);
            Optional<Privilege> privilege = privilegeRepo.findById(privilegeId);
            List<Basicactions> basicaction = basicactionsRepo.findByPrivilegeAndWorkclassAndDeletedFlag(privilege.get(),workclass.get(), 'N');
            basicactionsRepo.deleteAll(basicaction);
            return response;
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return  null;
        }
    }
}
