

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.example.demo.model.SearchQueryModel;

public class QuerySpeficiations {
	public static Specification<SearchQueryModel> nameContains(String name){
		return StringUtils.isEmpty(name) ? null : new Specification<SearchQueryModel>() {

			@Override
			public Predicate toPredicate(Root<SearchQueryModel> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.like(root.get("name"), "%" + name + "%");
			}
		};
	}
	
	public static Specification<SearchQueryModel> descriptionContains(String description){
		return StringUtils.isEmpty(description) ? null : new Specification<SearchQueryModel>() {

			@Override
			public Predicate toPredicate(Root<SearchQueryModel> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.like(root.get("description"), "%" + description + "%");
			}
		};
	}
	
	public static Specification<SearchQueryModel> priceGreaterThanEqual(long minPrice){
		return minPrice == 0 ? null : new Specification<SearchQueryModel>() {

			@Override
			public Predicate toPredicate(Root<SearchQueryModel> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
			}
		};
	}
	
	public static Specification<SearchQueryModel> priceLessThanEqual(long maxPrice){
		return maxPrice == 0 ? null : new Specification<SearchQueryModel>() {

			@Override
			public Predicate toPredicate(Root<SearchQueryModel> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
			}
		};
	}
	
	
}
