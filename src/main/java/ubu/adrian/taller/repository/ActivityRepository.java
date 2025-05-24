package ubu.adrian.taller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ubu.adrian.taller.model.Activity;
import ubu.adrian.taller.model.Event;

/**
 * Repositorio para las actividades
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
	public Activity findById(long activityID);
}
