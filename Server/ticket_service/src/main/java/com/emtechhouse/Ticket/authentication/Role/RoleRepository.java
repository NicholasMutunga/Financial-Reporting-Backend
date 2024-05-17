package com.emtechhouse.Ticket.authentication.Role;

import com.emtechhouse.Ticket.authentication.Role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    List<Role> findByEntityIdAndDeletedFlag(String entityId, Character flag);

    //Count Admin Roles
    @Transactional
    @Query(nativeQuery = true,value = "SELECT count(*) FROM roles where name ='ROLE_ADMIN'")
    int countRoles();
    Boolean existsByName(String rolename);

    //Select From user roles table
    @Transactional
    @Query(nativeQuery = true,value = "SELECT ur.user_id from roles rl join user_roles ur on rl.id=ur.role_id where role_id = :role_id")
    Long selectFlag(@Param("role_id") Long role_id);

    //Select From user roles table
    @Transactional
    @Query(nativeQuery = true,value = "SELECT * FROM roles WHERE name != 'ROLE_ADMIN' AND name != 'ROLE_USER' AND name != 'ROLE_HEAD'")
    List<Role> allCommitteeRoles();
}
