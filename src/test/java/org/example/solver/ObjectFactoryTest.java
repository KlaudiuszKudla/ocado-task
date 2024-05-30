package org.example.solver;

import org.example.exceptions.TooManyProductsException;
import org.example.exceptions.TooManyProvidersException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ObjectFactoryTest {

    private static final String ABSOLUTE_PATH_TO_CONFIG_FILE = "src/main/resources/config.json";
    private static final int MAX_PRODUCTS_IN_BASKET = 100;
    private static final int MAX_PRODUCTS_IN_CONFIG_FILE =1000;
    private static final int MAX_PROVIDERS_IN_CONFIG_FILE = 10;
    private ObjectFactory objectFactory;


    @BeforeEach
    public void init() {
        objectFactory = new ObjectFactory();
    }

    @ParameterizedTest
    @MethodSource("testCases")
    void testSplitMethod(List<String> products, Map<String, List<String>> expected) throws IOException, TooManyProvidersException, TooManyProductsException {
        Map<String, List<String>> result = objectFactory.split(products);
        assertEquals(expected, result);
    }

    private static Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of(
                        Arrays.asList("Cookies Oatmeal Raisin", "Cheese Cloth", "English Muffin"),
                        createExpectedResult("Parcel locker", Arrays.asList("English Muffin", "Cookies Oatmeal Raisin", "Cheese Cloth"))
                ),
                Arguments.of(
                        Arrays.asList("Chocolate - Unsweetened", "Shrimp - 21/25, Peel And Deviened",
                                "Cheese - St. Andre", "Sole - Dover, Whole, Fresh"),
                        createExpectedResult("In-store pick-up", Arrays.asList(
                                "Chocolate - Unsweetened", "Sole - Dover, Whole, Fresh","Cheese - St. Andre","Shrimp - 21/25, Peel And Deviened"))
                )
        );
    }

    private static Map<String, List<String>> createExpectedResult(String provider, List<String> items) {
        Map<String, List<String>> expected = new HashMap<>();
        expected.put(provider, items);
        return expected;
    }
    @Test
    void split_withTooManyProducts_shouldThrowTooManyProductsException() {
        List<String> items = Collections.nCopies(MAX_PRODUCTS_IN_BASKET + 1, "item");

        TooManyProductsException exception = assertThrows(TooManyProductsException.class, () -> {
            objectFactory.split(items);
        });
        assertEquals("Przekroczono limi przedmiotów w koszyku (100)", exception.getMessage());
    }

    @Test
    void CheckSizesOfConfigFile_Throw_TooManyProductsException(){

        Map<String, List<String>> itemsToProvidersMap = new HashMap<>();
        for (int i = 0; i <= MAX_PRODUCTS_IN_CONFIG_FILE; i++) {
            itemsToProvidersMap.put("item" + i, Arrays.asList("provider"));
        }
        TooManyProductsException exception = assertThrows(TooManyProductsException.class, () -> {
            objectFactory.checkSizesOfConfigFile(itemsToProvidersMap);
        });
        assertEquals("Przekroczono limit przedmiotów w pliku konfiguracyjnym (" + MAX_PRODUCTS_IN_CONFIG_FILE + ")", exception.getMessage());

    }







}