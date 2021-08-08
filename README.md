# MeetingsPlanner
Check and schedule a meeting

Create an application that will list the scheduled meetings ordered by start time for a given date. When no date is selected or on first launch, fetch the schedule for today’s date. 
User should be able to schedule meeting for a particular date and time if that slot is available.

Fetch the data from the following endpoint http://fathomless-shelf-5846.herokuapp.com/api/schedule?date="7/8/2015" 
by querying for a particular date in dd/mm/yyyy format.
You will receive a json of already scheduled meetings(booked slots) for that date.

Portrait Mode: Home.png
Landscape Mode:  HomeLandscape.png (Optional)


NEXT Button: Tapping over this button will fetch the scheduled meetings (request from API) for next date

PREV Button: Tapping over this button will fetch the scheduled meeting for prev date (request from API)

SCHEDULE COMPANY MEETING Button:
Tapping over this button will open a new screen with date field pre-filled from the list page.
Portrait Mode: Add Meeting.png
Landscape Mode: Add Meeting Landscape.png (Optional)
Can't schedule a meeting for past date (button should be disabled)

SCHEDULE COMPANY MEETING Form:
Use DatePicker for date, start time, end time selection.
On SUBMIT button click build a logic to check whether slot is available or not 
Show appropriate message to the user(“Slot available/Slot not available”). 
Note we don’t need to send an api request to actually schedule a meeting.
Api will return data for a given day in 24 hours format


Sample JSON

[
  {
    start_time: 11:00,
    end_time: 12:00,
    description: "development team of XYZ project: brainstorming session",
    participants: ["Akhil Gupta", "Sumit Arora", "Sakshi", "Sushant Sehgal"]
  },
  {
    start_time: 11:30,
    end_time: 12:00,
    description: "development team of XYZ project: brainstorming session",
    participants: ["Sahil Arora", "Neha"]
  },
  {
    start_time: 9:00,
    end_time: 10:30,
    description: "development team of XYZ project: brainstorming session",
    participants: ["Sushant Sehgal"]
  }
]
