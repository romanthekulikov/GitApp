package com.example.gitapp

import com.example.domain.domain.models.StargazerEntity
import com.example.domain.domain.models.UserEntity
import com.example.domain.domain.entity.Stared
import com.example.gitapp.ui.diagram.PeriodType
import com.example.domain.domain.use_cases.diagram.ConvertPeriodUseCase
import com.example.domain.domain.models.StaredModel
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ConvertPeriodUseCaseTest {
    private val convertPeriodUseCase: ConvertPeriodUseCase = ConvertPeriodUseCase()
    private val testUser1 = UserEntity("Filip", "null")
    private val testUser2 = UserEntity("Joni", "null")
    private val testUser3 = UserEntity("Mark", "null")

    private val testDate1 = LocalDate.parse("20240329", DateTimeFormatter.BASIC_ISO_DATE)
    private val testDate2 = LocalDate.parse("20240315", DateTimeFormatter.BASIC_ISO_DATE)
    private val testDate3 = LocalDate.parse("20240325", DateTimeFormatter.BASIC_ISO_DATE)
    private val testDate4 = LocalDate.parse("20240331", DateTimeFormatter.BASIC_ISO_DATE)
    private val testDate5 = LocalDate.parse("20240226", DateTimeFormatter.BASIC_ISO_DATE)
    @Test
    fun getPeriodStringTest() {
        val partData = mutableListOf<Stared>()
        val usersList = listOf(testUser1, testUser2, testUser3)
        partData.add(StaredModel(testDate1, usersList))
        val dayPeriod = convertPeriodUseCase.getPeriodString(partData, PeriodType.WEEK.toString())
        val weekPeriod = convertPeriodUseCase.getPeriodString(partData, PeriodType.MONTH.toString())
        val monthPeriod = convertPeriodUseCase.getPeriodString(partData, PeriodType.YEAR.toString())

        Assert.assertEquals("2024-03-29", dayPeriod)
        Assert.assertEquals("2024-03-25 <-> 2024-03-31", weekPeriod)
        Assert.assertEquals("2024-03-01 <-> 2024-03-31", monthPeriod)
    }

    @Test
    fun getStarredTest() {
        val stargazersList = mutableListOf<StargazerEntity>()
        stargazersList.add(
            StargazerEntity(testDate1, testUser1, "accompanist", "google")
        )
        stargazersList.add(
            StargazerEntity(testDate1, testUser2, "accompanist", "google")
        )
        stargazersList.add(
            StargazerEntity(testDate2, testUser3, "accompanist", "google")
        )
        val stared = convertPeriodUseCase.getStarred(stargazersList)
        val stared1 = stared[0]
        val stared2 = stared[1]

        Assert.assertEquals(stared1.time, testDate1)
        Assert.assertEquals(stared2.time, testDate2)
        Assert.assertEquals(stared1.users[1].nameUser, testUser2.nameUser)
        Assert.assertEquals(stared2.users[0].nameUser, testUser3.nameUser)
    }

    @Test
    fun getDataInPeriodTest() {
        val stargazersList = mutableListOf<StargazerEntity>()
        stargazersList.add(
            StargazerEntity(testDate1, testUser1, "accompanist", "google")
        )
        stargazersList.add(
            StargazerEntity(testDate2, testUser2, "accompanist", "google")
        )
        stargazersList.add(
            StargazerEntity(testDate3, testUser3, "accompanist", "google")
        )

        val stared = convertPeriodUseCase.getStarred(stargazersList)
        val partDataStared = convertPeriodUseCase.getDataInPeriod(testDate3, testDate4, stared)
        Assert.assertEquals(partDataStared.size, 2)
        Assert.assertEquals(partDataStared[0].time, testDate1)
        Assert.assertEquals(partDataStared[1].time, testDate3)
    }

    @Test
    fun getMonthWeekPeriodArrayTest() {
        val weekPeriod = convertPeriodUseCase.getMonthWeekPeriodArray(testDate5, testDate4)
        Assert.assertEquals(5, weekPeriod.size)
        Assert.assertEquals(weekPeriod[0], "26-3")
        Assert.assertEquals(weekPeriod[1], "4-10")
        Assert.assertEquals(weekPeriod[2], "11-17")
        Assert.assertEquals(weekPeriod[3], "18-24")
        Assert.assertEquals(weekPeriod[4], "25-31")
    }
}