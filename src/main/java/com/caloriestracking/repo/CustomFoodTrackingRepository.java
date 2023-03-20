package com.caloriestracking.repo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.caloriestracking.model.UserFoodTracking;
import com.caloriestracking.utils.CustomDateTimeUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomFoodTrackingRepository {

	private final EntityManager em;

	public List<UserFoodTracking> getTrackingDataWithOption(Long userId, Map<String, Integer> timeValues,
														LocalDateTime localDateTime, String reportType) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UserFoodTracking> cq = cb.createQuery(UserFoodTracking.class);
		Root<UserFoodTracking> root = cq.from(UserFoodTracking.class);

		// Danh sách các vị từ
		List<Predicate> timePredicates = new ArrayList<>();
		
		// where user.id = userId
		Predicate userIdEqual = cb.equal(root.get("user"), userId);
		timePredicates.add(userIdEqual);
		
		// Tìm kiếm theo tuần
		if (reportType.equals("WEEK")) {
			LocalDateTime firstDayOfTheWeek = CustomDateTimeUtils.getFirstDayOfWeek(localDateTime);
			List<LocalDateTime> allDaysOfTheWeek = CustomDateTimeUtils.getAllDaysOfTheWeek(firstDayOfTheWeek);
			LocalDateTime startDate = allDaysOfTheWeek.get(0);
			LocalDateTime endDate = allDaysOfTheWeek.get(allDaysOfTheWeek.size() - 1);
			Predicate betWeenDateTime = cb.between(root.get("consumedDatetime"), startDate, endDate);
			timePredicates.add(betWeenDateTime);
		} else {
			// where DAY(user.consumedDatetime) = timeValues.get("DAY") AND ...
			for (String timeKey : timeValues.keySet()) {
				timePredicates.add(
						cb.equal(cb.function(timeKey, Integer.class, root.get("consumedDatetime")),
								timeValues.get(timeKey)
						)
				);
			}
		}

		Predicate userIdAndTimePredicates = cb.and(timePredicates.toArray(new Predicate[0]));
		
		cq.select(root).where(userIdAndTimePredicates);

		TypedQuery<UserFoodTracking> query = em.createQuery(cq);
		return query.getResultList();

	}
}
