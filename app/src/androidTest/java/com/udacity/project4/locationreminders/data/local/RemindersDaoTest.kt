package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    private lateinit var database: RemindersDatabase

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()


    // Add testing implementation to the RemindersDao.kt
    @Test
    fun insertReminderAndGetById() = runBlockingTest {
        //Create a riminder
        val reminder = ReminderDTO(
            title = "title",
            description = "description",
            location = "location",
            latitude = 2.2,
            longitude = 12.12
        )

        // GIVEN - insert a task
        database.reminderDao().saveReminder(reminder)

        // WHEN - Get the task by id from the database
        val loaded = database.reminderDao().getReminderById(reminder.id)

        // THEN - The loaded data contains the expected values
        assertThat(loaded as ReminderDTO, notNullValue())
        assertThat(loaded.id, `is`(reminder.id))
        assertThat(loaded.title, `is`(reminder.title))
        assertThat(loaded.description, `is`(reminder.description))
        assertThat(loaded.location, `is`(reminder.location))
        assertThat(loaded.latitude, `is`(reminder.latitude))
        assertThat(loaded.longitude, `is`(reminder.longitude))
    }


    // Delete testing implementation to the RemindersDao.kt
    @Test
    fun deletedAllReminder() = runBlockingTest {
        //Create a riminder
        val reminder = ReminderDTO(
            title = "title",
            description = "description",
            location = "location",
            latitude = 2.2,
            longitude = 12.12
        )

        // GIVEN - insert a task
        database.reminderDao().saveReminder(reminder)
        database.reminderDao().deleteAllReminders()


        // WHEN - Get the task by id from the database
        val loaded = database.reminderDao().getReminders()

        // THEN - The loaded data contains the expected values
        assertThat(loaded, `is`(emptyList()))

    }

    @Test
    fun deletedByIdReminder() = runBlockingTest {
        //Create a riminder
        val reminder = ReminderDTO(
            title = "title",
            description = "description",
            location = "location",
            latitude = 2.2,
            longitude = 12.12
        )

        // GIVEN - insert and delete Reminder
        database.reminderDao().saveReminder(reminder)
        database.reminderDao().deleteReminder(reminder)

        // WHEN - Get the Reminder by id from the database
        val loaded = database.reminderDao().getReminderById(reminder.id)

        // THEN - The loaded data contains the expected values
        assertThat(loaded, nullValue())
    }

}