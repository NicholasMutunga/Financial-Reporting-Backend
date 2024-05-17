package com.emtechhouse.Ticket.category.CategoryController;

import com.emtechhouse.Ticket.Utils.EntityResponse;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.EntityRequestContext;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.UserRequestContext;
import com.emtechhouse.Ticket.category.CategoryModel.Category;
import com.emtechhouse.Ticket.category.CategoryRepository.CategoryRepository;
import com.emtechhouse.Ticket.category.CategoryService.CategoryService;
import com.emtechhouse.Ticket.priority.PriorityModel.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("system/ticketing/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else
            {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
                else
                {
                    Optional<Category> checkCategory = categoryRepository.findByEntityIdAndCategoryCodeAndDeletedFlag(EntityRequestContext.getCurrentEntityId(), category.getCategoryCode(), 'N');
                    if (checkCategory.isPresent()) {
                        EntityResponse response = new EntityResponse();
                        response.setMessage("CATEGORY WITH CODE " + checkCategory.get().getCategoryCode() + " AND NAME " + checkCategory.get().getTitle() + " ALREADY CREATED ON " + checkCategory.get().getPostedTime());
                        response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        category.setPostedBy(UserRequestContext.getCurrentUser());
                        category.setEntityId(EntityRequestContext.getCurrentEntityId());
                        category.setPostedFlag('Y');
                        category.setModifiedBy("System");
                        category.setPostedTime(new Date());
                        Category newCategory = categoryService.addCategory(category);
                        EntityResponse response = new EntityResponse();
                        response.setMessage("CATEGORY WITH CODE " + category.getCategoryCode() + " AND NAME " + category.getTitle() + " CREATED SUCCESSFULLY AT " + category.getPostedTime());
                        response.setStatusCode(HttpStatus.CREATED.value());
                        response.setEntity(newCategory);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }


    @PutMapping("/modify")
    public ResponseEntity<?> updateCategory(@RequestBody Category category) {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else
            {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else
                {
                    category.setModifiedBy(UserRequestContext.getCurrentUser());
                    category.setEntityId(EntityRequestContext.getCurrentEntityId());
                    Optional<Category> theCategory = categoryRepository.findById(category.getId());
                    if (theCategory.isPresent()) {
                        category.setPostedTime(theCategory.get().getPostedTime());
                        category.setPostedFlag(theCategory.get().getPostedFlag());
                        category.setPostedBy(theCategory.get().getPostedBy());
                        category.setModifiedFlag('Y');
                        category.setVerifiedFlag('N');
                        category.setModifiedTime(new Date());
                        category.setModifiedBy(category.getModifiedBy());
                        categoryService.updateCategory(category);
                        EntityResponse response = new EntityResponse();
                        log.info("CATEGORY WITH CODE " + category.getCategoryCode() + " AND NAME: " + category.getTitle() + " MODIFIED SUCCESSFULLY AT " + category.getModifiedTime());
                        response.setMessage("CATEGORY WITH CODE " + category.getCategoryCode() + " AND NAME " + category.getTitle() + " MODIFIED SUCCESSFULLY AT " + category.getModifiedTime());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(category);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                        response.setStatusCode(HttpStatus.NOT_FOUND.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }



    @GetMapping("/all")
    public ResponseEntity<?> getAllMissectors() {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else
            {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else
                {
                    List<Category> category = categoryRepository.findAllByEntityIdAndDeletedFlagOrderByIdDesc(EntityRequestContext.getCurrentEntityId(), 'N');
                    if (category.size() > 0) {
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.FOUND.getReasonPhrase());
                        response.setStatusCode(HttpStatus.FOUND.value());
                        response.setEntity(category);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                        response.setStatusCode(HttpStatus.NOT_FOUND.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }

                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }


    @GetMapping("/find/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Long id) {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    Category category = categoryService.findById(id);
                    EntityResponse response = new EntityResponse();
                    response.setMessage(HttpStatus.OK.getReasonPhrase());
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setEntity(category);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }


    @GetMapping("/find/by/category/{code}")
    public ResponseEntity<?> getCategoryByCode(@PathVariable("code") String categoryCode) {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    Optional<Category> searchCode = categoryRepository.findByEntityIdAndCategoryCode(EntityRequestContext.getCurrentEntityId(), categoryCode);
                    if (searchCode.isPresent()) {
                        EntityResponse response = new EntityResponse();
                        Optional<Category> category = categoryRepository.findByCategoryCode(categoryCode);
                        response.setMessage("CATEGORY WITH CODE " + categoryCode + " ALREADY REGISTERED");
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(category);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        EntityResponse response = new EntityResponse();
                        response.setMessage("CATEGORY WITH CODE " + categoryCode + " AVAILABLE FOR REGISTRATION");
                        response.setStatusCode(HttpStatus.NOT_FOUND.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }

                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }



    @PutMapping("/verify/{id}")
    public ResponseEntity<?> verify(@PathVariable String id) {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else
            {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else
                {
                    Optional<Category> theCategory = categoryRepository.findById(Long.parseLong(id));
                    if (theCategory.isPresent()) {
                        Category category = theCategory.get();
                        if (category.getPostedBy().equalsIgnoreCase(UserRequestContext.getCurrentUser())) {
                            EntityResponse response = new EntityResponse();
                            response.setMessage("You Can Not Verify What you initiated");
                            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                            response.setEntity("");
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        } else
                        if (category.getModifiedBy().equalsIgnoreCase(UserRequestContext.getCurrentUser())) {
                            EntityResponse response = new EntityResponse();
                            response.setMessage("You Can Not Verify What you Modified");
                            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                            response.setEntity("");
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        }else
                        {
                            if (category.getVerifiedFlag().equals('Y')) {
                                EntityResponse response = new EntityResponse();
                                log.info("CATEGORY WITH CODE " + category.getCategoryCode() + " AND NAME " + category.getTitle() + " ALREADY VERIFIED");
                                response.setMessage("CATEGORY WITH CODE " + category.getCategoryCode() + " AND NAME " + category.getTitle() + " ALREADY VERIFIED");
                                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                                response.setEntity("");
                                return new ResponseEntity<>(response, HttpStatus.OK);
                            } else {
                                {
                                    category.setVerifiedFlag('Y');
                                    category.setVerifiedTime(new Date());
                                    category.setVerifiedBy(UserRequestContext.getCurrentUser());
                                    category.setEntityId(EntityRequestContext.getCurrentEntityId());
                                    categoryRepository.save(category);
                                    EntityResponse response = new EntityResponse();
                                    log.info("CATEGORY WITH CODE " + category.getCategoryCode() + " AND NAME: " + category.getTitle() + " VERIFIED SUCCESSFULLY AT " + category.getVerifiedTime());
                                    response.setMessage("CATEGORY WITH CODE " + category.getCategoryCode() + " AND NAME " + category.getTitle() + " VERIFIED SUCCESSFULLY AT " + category.getVerifiedTime());
                                    response.setStatusCode(HttpStatus.OK.value());
                                    response.setEntity(category);
                                    return new ResponseEntity<>(response, HttpStatus.OK);
                                }

                            }

                        }

                    } else {
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                        response.setStatusCode(HttpStatus.NOT_FOUND.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else
            {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else
                {
                    Optional<Category> theCategory = categoryRepository.findById(id);
                    if (theCategory.isPresent()) {
                        Category category = theCategory.get();
                        category.setDeletedFlag('Y');
                        category.setDeletedTime(new Date());
                        category.setDeletedBy(UserRequestContext.getCurrentUser());
                        category.setEntityId(EntityRequestContext.getCurrentEntityId());
                        categoryRepository.save(category);
                        EntityResponse response = new EntityResponse();
                        log.info("CATEGORY WITH CODE " + category.getCategoryCode() + " AND NAME " + category.getTitle() + " DELETED SUCCESSFULLY AT " + category.getDeletedTime());
                        response.setMessage("CATEGORY WITH CODE " + category.getCategoryCode() + " AND NAME " + category.getTitle() + " DELETED SUCCESSFULLY AT " + category.getDeletedTime());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(category);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                        response.setStatusCode(HttpStatus.NOT_FOUND.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

}
