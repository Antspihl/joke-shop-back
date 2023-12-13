package ee.veebiprojekt.veebiprojekttest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ee.veebiprojekt.veebiprojekttest.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchDTO {
    private Long userId;
    private String username;
    private String email;
    private String fullName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Builder.Default
    private Integer limit = 10;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Builder.Default
    private Integer page = 0;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Builder.Default
    private String sort = "id";
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Builder.Default
    private Sort.Direction dir = Sort.DEFAULT_DIRECTION;

    public void addFilters(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, List<Predicate> filters) {
        if (userId != null) {
            Predicate lastNameAsPredicate = criteriaBuilder.like(root.get("userId"), "%" + userId + "%");
            filters.add(lastNameAsPredicate);
        }
        if (username != null) {
            Predicate lastNameAsPredicate = criteriaBuilder.like(root.get("username"), "%" + username + "%");
            filters.add(lastNameAsPredicate);
        }
        if (email != null) {
            Predicate lastNameAsPredicate = criteriaBuilder.like(root.get("email"), "%" + email + "%");
            filters.add(lastNameAsPredicate);
        }
        if (fullName != null) {
            Predicate lastNameAsPredicate = criteriaBuilder.like(root.get("fullName"), "%" + fullName + "%");
            filters.add(lastNameAsPredicate);
        }
    }

    public Specification<User> getSpecification() {
        return (root, query, criteriaBuilder) -> {query.distinct(true);
            Predicate noFiltersApplied = criteriaBuilder.conjunction();
            List<Predicate> filters = new ArrayList<>();
            filters.add(noFiltersApplied);
            addFilters(root, query, criteriaBuilder, filters);
            return criteriaBuilder.and(filters.toArray(new Predicate[0]));
        };
    }

    public Pageable getPageable() {
        return PageRequest.of(
                (page != null) ? page : 0,
                (limit != null && limit >= 0) ? limit : 10,
                getSortSpec()
        );
    }

    public Sort getSortSpec() {
        if (sort == null) return Sort.unsorted();
        return (dir != null && dir == Sort.Direction.DESC) ?
                Sort.by(sort).descending() : Sort.by(sort).ascending();
    }
}