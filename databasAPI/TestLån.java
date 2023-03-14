package databasAPI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TestLån {
    private static Logger logger = LogManager.getLogger(TestKonto.class.getName());

    @Nested
    @DisplayName("Test för ärAvstängd")
    class ärFörsenad {

        @Test
        @DisplayName("lån gjordes 2090-01-01, och ska återlämnas 2090-02-01: false")
        void testärFörsenad1() {
                   Lån lån = new Lån(1,1111,LocalDate.of(2023,1,1),LocalDate.of(2090,2,1));
            assertFalse(lån.ärFörsenad());
        }

        @Test
        @DisplayName("Viktor har ingen avstängning. returns false")
        void testärFörsenad2() {
            Lån lån = new Lån(1,1111,LocalDate.of(2023,1,1),LocalDate.of(2023,2,1));
            assertTrue(lån.ärFörsenad());
        }
    }
    @Nested
    @DisplayName("Test för ärAvstängd")
    class toString {
        @Test
        @DisplayName("skriver ut lån och strängen stämmer med output.")
        void testToString() {
            Lån lån = new Lån(1, 1111, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 1));
            assertTrue(lån.ärFörsenad());
        }
    }
    }