package com.fcs.marathonbademo.repository;

import com.fcs.marathonbademo.entity.GameCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameCharacterRepository extends JpaRepository<GameCharacter, Integer> {
}
