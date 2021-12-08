package com.example.mohammadabumusarabiul.unit;

import com.example.mohammadabumusarabiul.util.DateTimeHelper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class DateTimeHelperUnitTest {
    final private DateTimeHelper dateTimeHelper = new DateTimeHelper();

    @Test
    public void whenTargetDateTime_EqualToStartDateTime_shouldReturn_false() {
        //Given
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.minusMinutes(1);

        //When
        var result = dateTimeHelper.isWithinRange(startDateTime, startDateTime, endDateTime);

        //Then
        Assertions.assertEquals(false, result);
    }

    @Test
    public void whenTargetDateTime_AfterStartDateTime_shouldReturn_false() throws InterruptedException {
        //Given
        LocalDateTime startDateTime = LocalDateTime.now();
        Thread.sleep(10);
        LocalDateTime targetDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.minusMinutes(1);

        //When
        var result = dateTimeHelper.isWithinRange(targetDateTime, startDateTime, endDateTime);

        //Then
        Assertions.assertFalse(result);
    }

    @Test
    public void whenTargetDateTime_BeforeEndDateTime_shouldReturn_false() {
        //Given
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime targetDateTime = startDateTime.minusMinutes(2);
        LocalDateTime endDateTime = startDateTime.minusMinutes(1);

        //When
        var result = dateTimeHelper.isWithinRange(targetDateTime, startDateTime, endDateTime);

        //Then
        Assertions.assertFalse(result);
    }

    @Test
    public void whenTargetDateTime_EqualEndDateTime_shouldReturn_true() {
        //Given
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.minusMinutes(1);

        //When
        var result = dateTimeHelper.isWithinRange(endDateTime, startDateTime, endDateTime);

        //Then
        Assertions.assertTrue(result);
    }

    @Test
    public void whenTargetDateTime_BetweenStartAndEndDateTime_shouldReturn_true(){
        //Given
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime targetDateTime = startDateTime.minusMinutes(1);
        LocalDateTime endDateTime = startDateTime.minusMinutes(2);

        //When
        var result = dateTimeHelper.isWithinRange(targetDateTime, startDateTime, endDateTime);

        //Then
        Assertions.assertTrue(result);
    }

    @Test
    public void whenTargetDateTime_BeforeStartDateTimeButBeforeEndDateTime_shouldReturn_false() {
        //Given
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime targetDateTime = startDateTime.minusMinutes(3);
        LocalDateTime endDateTime = startDateTime.minusMinutes(2);

        //When
        var result = dateTimeHelper.isWithinRange(targetDateTime, startDateTime, endDateTime);

        //Then
        Assertions.assertFalse(result);
    }

    @Test
    public void whenTargetDateTime_AfterStartDateTimeButAfterEndDateTime_shouldReturn_false() {
        //Given

        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime targetDateTime= startDateTime.plusMinutes(1);
        LocalDateTime endDateTime = targetDateTime.minusMinutes(1);

        //When
        var result = dateTimeHelper.isWithinRange(targetDateTime, startDateTime, endDateTime);

        //Then
        Assertions.assertFalse(result);
    }

    @Test
    public void whenTargetDateTime_AfterEndDateTime_Then_isInDeleteRange_shouldReturn_false() {
        //Given
        LocalDateTime targetDateTime= LocalDateTime.now();
        LocalDateTime endDateTime = targetDateTime.minusMinutes(1);

        //When
        var result = dateTimeHelper.isInDeleteRange(targetDateTime, endDateTime);

        //Then
        Assertions.assertFalse(result);
    }

    @Test
    public void whenTargetDateTime_BeforeEndDateTime_Then_isInDeleteRange_shouldReturn_true() {
        //Given
        LocalDateTime targetDateTime= LocalDateTime.now();
        LocalDateTime endDateTime = targetDateTime.plusMinutes(1);

        //When
        var result = dateTimeHelper.isInDeleteRange(targetDateTime, endDateTime);

        //Then
        Assertions.assertTrue(result);
    }

    @Test
    public void whenTargetDateTime_EqualEndDateTime_Then_isInDeleteRange_shouldReturn_true() {
        //Given
        LocalDateTime targetDateTime= LocalDateTime.now();

        //When
        var result = dateTimeHelper.isInDeleteRange(targetDateTime, targetDateTime);

        //Then
        Assertions.assertTrue(result);
    }
}
