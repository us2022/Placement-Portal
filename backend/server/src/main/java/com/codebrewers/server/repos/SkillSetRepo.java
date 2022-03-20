package com.codebrewers.server.repos;

import com.codebrewers.server.models.SkillSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillSetRepo extends JpaRepository<SkillSet, Long> {
}
