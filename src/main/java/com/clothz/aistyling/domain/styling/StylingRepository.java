package com.clothz.aistyling.domain.styling;

import com.clothz.aistyling.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StylingRepository extends JpaRepository<Styling, Long> {
}
