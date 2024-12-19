package com.srishasti.repo;

import com.srishasti.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Integer> {

    List<Task> findByUserId(int userId);

    @Query(value = "select * from Task where status = 'pending' and deadline < curdate()", nativeQuery = true)
    List<Task> getOverdueTasks();
}
