package com.lpu.mind_maze_ai.listener;

import com.lpu.mind_maze_ai.entity.Auditable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Entity listener that automatically handles auditing fields for entities that extend {@link Auditable}.
 * This listener sets the created/updated by fields using the currently authenticated user's information.
 *
 * <p>The listener is triggered by JPA lifecycle events:
 * <ul>
 *   <li>{@code PrePersist} - Before an entity is persisted for the first time</li>
 *   <li>{@code PreUpdate} - Before an entity is updated</li>
 * </ul>
 * </p>
 *
 * <p>If no authenticated user is found, the default value "SYSTEM" is used.</p>
 */
public class AuditableEntityListener {

    /**
     * Sets the createdBy and updatedBy fields when an entity is first persisted.
     * This method is automatically called by JPA before the entity is inserted into the database.
     *
     * @param auditable The entity being persisted that extends {@link Auditable}
     */
    @PrePersist
    public void setCreatedBy(Auditable auditable) {
        String currentUser = getCurrentUser();
        auditable.setCreatedBy(currentUser);
        auditable.setUpdatedBy(currentUser);
    }

    /**
     * Updates the updatedBy field when an entity is modified.
     * This method is automatically called by JPA before the entity is updated in the database.
     *
     * @param auditable The entity being updated that extends {@link Auditable}
     */
    @PreUpdate
    public void setUpdatedBy(Auditable auditable) {
        String currentUser = getCurrentUser();
        auditable.setUpdatedBy(currentUser);
    }

    /**
     * Retrieves the username of the currently authenticated user from Spring Security context.
     * If no authenticated user is found, returns "SYSTEM" as the default value.
     *
     * <p>This method checks both the SecurityContext and Authentication objects to ensure
     * they exist before attempting to retrieve the username.</p>
     *
     * @return The username of the currently authenticated user or "SYSTEM" if none found
     */
    private String getCurrentUser() {
        // Fetch the currently authenticated user from Spring Security
        if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }
        // Return a default value if no user is authenticated
        return "SYSTEM";
    }
}
