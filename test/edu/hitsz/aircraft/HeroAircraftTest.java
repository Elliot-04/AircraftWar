package edu.hitsz.aircraft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeroAircraftTest {

    private HeroAircraft heroAircraft;

    @BeforeEach
    void setUp() {
        heroAircraft = HeroAircraft.getInstance();
        heroAircraft.increaseHp(100);
    }

    @Test
    @DisplayName("Test getInstance method for Singleton pattern")
    void getInstance() {
        HeroAircraft instance1 = HeroAircraft.getInstance();
        HeroAircraft instance2 = HeroAircraft.getInstance();
        assertSame(instance1, instance2, "getInstance() should return the same instance");
    }

    @Test
    @DisplayName("Test decreaseHp method")
    void decreaseHp() {
        int initialHp = heroAircraft.getHp();

        // 测试正常减少
        heroAircraft.decreaseHp(30);
        assertEquals(initialHp - 30, heroAircraft.getHp(), "Hp should decrease by 30");

        // 测试生命值降到0以下的情况
        heroAircraft.decreaseHp(200);
        assertEquals(0, heroAircraft.getHp(), "Hp should be 0 when decreased below zero");
        assertTrue(heroAircraft.notValid(), "HeroAircraft should be invalid when hp is 0");
    }

    @Test
    @DisplayName("Test increaseHp method")
    void increaseHp() {
        int maxHp = heroAircraft.getHp();

        // 测试增加生命值不超过上限
        heroAircraft.decreaseHp(40);
        int currentHp = heroAircraft.getHp();
        heroAircraft.increaseHp(10);
        assertEquals(currentHp + 10, heroAircraft.getHp(), "Hp should increase by 10");

        // 测试增加生命值超过上限的情况
        heroAircraft.increaseHp(300);
        assertEquals(maxHp, heroAircraft.getHp(), "Hp should not exceed maxHp");
    }
}