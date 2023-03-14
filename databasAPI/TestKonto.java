package databasAPI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
public class TestKonto {
    private static Logger logger = LogManager.getLogger(TestKonto.class.getName());
    @Nested
    @DisplayName("Test för ärAvstängd")
    class ärAvstängd {
        @Test
        @DisplayName("Viktor har avstängning 2099-01-01. returns true")
        void testärAvstängd1() {
            Konto medlem = new Konto(
                    "Viktor",
                    "Sjögren",
                    3412121212L,
                    0,
                    1111,
                    LocalDate.of(2090,1,1),
                    new Lån[]{},
                    0,
                    0);

            assertTrue(medlem.ärAvstängd());
        }
        @Test
        @DisplayName("Viktor har avstängning 2010-01-01. returns false")
        void testärAvstängd2() {
            Konto medlem = new Konto(
                    "Viktor",
                    "Sjögren",
                    3412121212L,
                    0,
                    1111,
                    LocalDate.of(2010,1,1),
                    new Lån[]{},
                    0,
                    0);

            assertTrue(medlem.ärAvstängd());
        }
        @Test
        @DisplayName("Viktor har avstängning 2099-01-01. returns true")
        void testärAvstängd3() {
            Konto medlem = new Konto(
                    "Viktor",
                    "Sjögren",
                    3412121212L,
                    0,
                    1111,
                    LocalDate.of(2090,1,1),
                    new Lån[]{},
                    0,
                    0);

            assertTrue(medlem.ärAvstängd());
        }
    }
        @Test
        @DisplayName("Viktor har gammal avstängning 2010-01-01. returns false")
        void testärAvstängd2() {

        }

        @Test
        @DisplayName("Viktor har ingen avstängning. returns true")
        void testärAvstängd3() {

        }

        @Nested
        @DisplayName("Test för ärAvstängd")
        class börAvstängas {
        }

        @Nested
        @DisplayName("Test för ärAvstängd")
        class börSvartlistas {
        }

        @Nested
        @DisplayName("Test för ärAvstängd")
        class harLån {
        }

        @Nested
        @DisplayName("Test för ärAvstängd")
        class harMaxLån {
        }

        @Nested
        @DisplayName("Test för ärAvstängd")
        class getMaxLån {
        }
    }

