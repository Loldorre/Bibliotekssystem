package databasAPI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

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

            assertFalse(medlem.ärAvstängd());
        }
        @Test
        @DisplayName("Viktor har ingen avstängning. returns false")
        void testärAvstängd3() {
            Konto medlem = new Konto(
                    "Viktor",
                    "Sjögren",
                    3412121212L,
                    0,
                    1111,
                    null,
                    new Lån[]{},
                    0,
                    0);

            assertFalse(medlem.ärAvstängd());
        }

    }

        @Nested
        @DisplayName("Test för börAvstängas")
        class börAvstängas {
            @Test
            @DisplayName("Viktor har 3 förseningar. returns true")
            void testbörAvstängas1() {
                Konto medlem = new Konto(
                        "Viktor",
                        "Sjögren",
                        3412121212L,
                        0,
                        1111,
                        LocalDate.of(2090,1,1),
                        new Lån[]{},
                        0,
                        3
                );

                assertTrue(medlem.börAvstängas());
            }
            @Test
            @DisplayName("Viktor har 2 förseningar. returns false")
            void testbörAvstängas2() {
                Konto medlem = new Konto(
                        "Viktor",
                        "Sjögren",
                        3412121212L,
                        0,
                        1111,
                        LocalDate.of(2090,1,1),
                        new Lån[]{},
                        0,
                        2
                );
                assertFalse(medlem.börAvstängas());
            }
        }

        @Nested
        @DisplayName("Test för börSvartlistas")
        class börSvartlistas {
            @Test
            @DisplayName("Viktor har 1 avstängning. returns true")
            void testbörbörSvartlistas1() {
                Konto medlem = new Konto(
                        "Viktor",
                        "Sjögren",
                        3412121212L,
                        0,
                        1111,
                        LocalDate.of(2090,1,1),
                        new Lån[]{},
                        2,
                        3
                );

                assertTrue(medlem.börSvartlistas());
            }
            @Test
            @DisplayName("Viktor har 2 avstängningar. returns true")
            void testbörbörSvartlistas2() {
                Konto medlem = new Konto(
                        "Viktor",
                        "Sjögren",
                        3412121212L,
                        0,
                        1111,
                        LocalDate.of(2090,1,1),
                        new Lån[]{},
                        2,
                        3
                );

                assertTrue(medlem.börSvartlistas());
            }
        }

        @Nested
        @DisplayName("Test för harLån")
        class harLån {
        @Test
        @DisplayName("Viktor har 1 lån. returns true")
        void testharLån1() {
            Konto medlem = new Konto(
                    "Viktor",
                    "Sjögren",
                    3412121212L,
                    0,
                    1111,
                    LocalDate.of(2090,1,1),
                    new Lån[]{new Lån(1,1111,LocalDate.now(),LocalDate.of(2045,1,1))},
                    2,
                    3
            );

            assertTrue(medlem.harLån());
        }
            @Test
            @DisplayName("Viktor har 0 lån. returns true")
            void testharLån2() {
                Konto medlem = new Konto(
                        "Viktor",
                        "Sjögren",
                        3412121212L,
                        0,
                        1111,
                        LocalDate.of(2090,1,1),
                        new Lån[]{},
                        2,
                        3
                );
                assertFalse(medlem.harLån());
            }
        }

        @Nested
        @DisplayName("Test för harMaxlån")
        class harMaxLån {
            @Test
            @DisplayName("Liguster har 3 lån. returns false")
            void testharLån1() {
                Konto medlem = new Konto(
                        "Liguster",
                        "Larsson",
                        3412121212L,
                        0,
                        1111,
                        LocalDate.of(2090,1,1),
                        new Lån[]{
                                new Lån(1,1111,LocalDate.now(),LocalDate.of(2045,1,1)),
                                new Lån(2,1111,LocalDate.now(),LocalDate.of(2045,1,1)),
                                new Lån(3,1111,LocalDate.now(),LocalDate.of(2045,1,1))
                        },
                        2,
                        3
                );
                assertTrue(medlem.harMaxLån());
            }
            @Test
            @DisplayName("Liguster har 3 lån. returns true")
            void testharLån2() {
                Konto medlem = new Konto(
                        "Liguster",
                        "Larsson",
                        3412121212L,
                        0,
                        1111,
                        LocalDate.of(2090,1,1),
                        new Lån[]{
                                new Lån(1,1111,LocalDate.now(),LocalDate.of(2045,1,1))
                        },
                        2,
                        3
                );
                assertFalse(medlem.harMaxLån());
            }
        }

        @Nested
        @DisplayName("Test för getMaxLån")
        class getMaxLån {
            @Test
            @DisplayName("Liguster är kandidat student. returns 3")
            void testgetMaxLån1() {
                Konto medlem = new Konto(
                        "Liguster",
                        "Larsson",
                        3412121212L,
                        0,
                        1111,
                        LocalDate.of(2090,1,1),
                        new Lån[]{ },
                        2,
                        3
                );
                assertEquals(3,medlem.getMaxLån());
            }
            @Test
            @DisplayName("Liguster är master student. returns 5")
            void testgetMaxLån2() {
                Konto medlem = new Konto(
                        "Liguster",
                        "Larsson",
                        3412121212L,
                        1,
                        1111,
                        LocalDate.of(2090,1,1),
                        new Lån[]{ },
                        2,
                        3
                );
                assertEquals(5,medlem.getMaxLån());
            }

    @Test
    @DisplayName("Liguster är doktorand. returns 7")
    void testgetMaxLån3() {
        Konto medlem = new Konto(
                "Liguster",
                "Larsson",
                3412121212L,
                2,
                1111,
                LocalDate.of(2090, 1, 1),
                new Lån[]{},
                2,
                3
        );
        assertEquals(7, medlem.getMaxLån());
    }
            @Test
            @DisplayName("Liguster är postdoc eller lärare. returns 10")
            void testgetMaxLån4() {
                Konto medlem = new Konto(
                        "Liguster",
                        "Larsson",
                        3412121212L,
                        3,
                        1111,
                        LocalDate.of(2090, 1, 1),
                        new Lån[]{},
                        2,
                        3
                );
                assertEquals(10, medlem.getMaxLån());
            }
        }
    }

