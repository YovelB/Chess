#include <Wire.h>
#include <Thread.h>
#include <RtcDS1307.h>
#include <LiquidCrystal_I2C.h>
LiquidCrystal_I2C lcd(0x27, 2, 1, 0, 4, 5, 6, 7, 3, POSITIVE);

Thread messagesTask = Thread();
RtcDS1307<TwoWire> Rtc(Wire);
RtcDateTime now1;
RtcDateTime dtLimit;
RtcDateTime dtWhitePlayer;
RtcDateTime dtBlackPlayer;
RtcDateTime dtStart;

String moveMessage;
bool messageDirection;
bool firstDisplay1, firstDisplay2;
bool started;

void setup () {
  messagesTask.onRun(messageCallback);
  moveMessage = ""; //the move message that is sent with new line
  messageDirection = true; //true is from computer 1 to 2. false is from 2 to 1
  Serial.begin(115000); //representing arduino
  Serial1.begin(115000); //representing computer 1
  Serial2.begin(115000); //representing computer 2
  firstDisplay1 = true;
  firstDisplay2 = true;
  started = false;
  dtStart = RtcDateTime(0, 0, 0, 0, 0, 0);
  dtLimit = RtcDateTime(0, 0, 0, 0, 15, 0);
  
  lcd.begin(20,4);
  Rtc.Begin();
  Rtc.SetDateTime(dtStart);
  Rtc.SetIsRunning(false);
  now1 = Rtc.GetDateTime();
  dtWhitePlayer = dtLimit - now1;
  dtBlackPlayer = dtLimit - now1;
  lcd.setCursor(0, 0);
  lcdprintDateTime("White" ,dtWhitePlayer);
  lcd.setCursor(0, 1);
  lcdprintDateTime("Black" ,dtBlackPlayer);
}

void loop () {
  if(messagesTask.shouldRun()) {
    messagesTask.run();
  }
  
  if (started) {
    if (!Rtc.GetIsRunning()) {
      // RTC was not actively running, starting now
      Rtc.SetIsRunning(true);
    }
    maintainDisplay();
  }
}

#define countof(a) (sizeof(a) / sizeof(a[0]))

void lcdprintDateTime(char Player[], const RtcDateTime& dt) {
    char datestring[20];
    snprintf_P(datestring,
            countof(datestring),
            PSTR("%s time: %02u:%02u"),
            Player,
            dt.Minute(),
            dt.Second());
    lcd.print(datestring);
}

void maintainDisplay() {
  static const unsigned long REFRESH_INTERVAL = 1000; // ms
  static unsigned long lastRefreshTime = 0;
  
  if(millis() - lastRefreshTime >= REFRESH_INTERVAL) {
    lastRefreshTime += REFRESH_INTERVAL;
    refreshDisplay();
  }
}

void refreshDisplay() {
  //time out
  if (now1 > dtLimit) {
    Rtc.SetIsRunning(false);
    started = false;
    lcd.setCursor(0, 2);
    dtStart = RtcDateTime(0, 0, 0, 0, 0, 0);
    Rtc.SetDateTime(dtStart);
    dtWhitePlayer = dtLimit - now1;
    dtBlackPlayer = dtLimit - now1;
    lcd.print("     Time Out"); // spaces for centering message
  } else {
    if(messageDirection) {
      if(firstDisplay1) {
        firstDisplay1 = false;
      } else {
        now1 = Rtc.GetDateTime();
      }
      dtWhitePlayer = dtLimit - now1; 
    } else {
      if(firstDisplay2) {
        firstDisplay2 = false;
      } else {
        dtBlackPlayer = dtLimit - now1;
      }
    }
    lcd.setCursor(0, 0);
    lcdprintDateTime("White" ,dtWhitePlayer);
    lcd.setCursor(0, 1);
    lcdprintDateTime("Black" ,dtBlackPlayer);
  }
  now1 = Rtc.GetDateTime();
}

void messageCallback() {
  moveMessage = "";
  //checking if computer that is connected 
  //to the bluetooth sends messages to the arduino
  if (Serial1.available() > 0 || Serial2.available() > 0) { 
    // checking if someone(computer 1 or 2) is sending data
    if (Serial1.available() > 0) { //if it is computer1 is sending data
      moveMessage += (char)Serial1.read(); //saving the message sent to a string
      messageDirection = true;
    } else {
      moveMessage += (char)Serial2.read();
      messageDirection = false;
    }
  }

  //receiving messeges from computer or sending 
  //the the message from the arduino to the computer
  if (moveMessage.length()) {
    started = true;
    if (messageDirection) { //checking the direction
      Serial2.print(moveMessage);
    } else {
      Serial1.print(moveMessage);
    }
    moveMessage = ""; //reseting the string
  }
}
