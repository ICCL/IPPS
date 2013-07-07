#include <Ethernet.h>
#include <SPI.h>
#include <SHT1x.h>
#include <stdlib.h>

  #define dataPin  6
  #define clockPin 7
  
  byte mac[] = {0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED}; // 乙太網路
  IPAddress ip(192, 168, 0, 30);                     // local IP
  IPAddress remote(192, 168, 0, 119);                // remote IP
  EthernetServer Listen(80);                         // server
  EthernetClient client;                             // client
  
  SHT1x sht1x(dataPin, clockPin);                    // 傳感器
  
  boolean EthernetTf = true;
  String Request;                                    // stores the HTTP request
  float Temperature, Humidity;                       // 溫度、濕度
  int Soil, Light;                                   // 土壤, 光
  
  float safe_Temperature, safe_Humidity;             // safety value
  int safe_Soil, safe_Light;                         // saftey value
  
  static int Auto = 1;
  static int Manually = 2;
  int Status = Auto;
  
  String tempLamp, tempFan, tempSprinkler;
  int time = 0;
  
  

  void setup() {
    Serial.begin(9600);
    
    pinMode(A10, INPUT);            //指撥開關1控制上傳
    pinMode(A11, INPUT);            //指撥開關4控制監聽
    
    pinMode(A0, INPUT);             // 設定腳位位置的行為
    pinMode(9, OUTPUT);             // 同上
    pinMode(2, OUTPUT);             // 同上
    pinMode(3, OUTPUT);             // 同上
    
    Ethernet.begin(mac, ip);
    Listen.begin();
    Serial.print("server is at ");
    Serial.println(Ethernet.localIP());
    
    /* default safe value*/
    safe_Temperature = 26;
    safe_Humidity    = 69;
    safe_Soil        = 19;
    safe_Light       = 299;
    Init_Safetys();
    SetController("Status", "Auto");
  }
  
  void loop() {
    int Send   = analogRead(A10);           // 光
    int Listen = analogRead(A11);           // 光
    
    Controller();
    
    if(Send < 10 ) {
      //EthernetSend();
    }
    
    if(Listen < 10) {
     //EthernetListen();
    } 
    EthernetListen();
    //Serial.print("Send: ");
    //Serial.print(Send);
    //Serial.print("  Listen: ");
    //Serial.println(Listen);
    delay(500);
  }
  
  void EthernetSend() {
    if(time == 20) { 
      Deliver();
      time = 0;
    }
    time++;
  }
  
  void Init_Safetys() {
    if (client.connect(remote, 80)) {
      Serial.println("connected");
      client.print("GET /ipps/api/safetys/");
      client.println(" HTTP/1.0");
      client.println();

      client.stop();
      client.flush();
      Serial.println("Update Safetys!!");
    } else {
      Serial.println("connection failed");
    }
  }
  
  void Controller() {
    Temperature = sht1x.readTemperatureC(); // 溫度傳感器
    Humidity    = sht1x.readHumidity();     // 濕度傳感器
    int loo     = analogRead(A1);
    Soil        = (1000-(loo-23))/10;       // 土壤
    Light       = analogRead(A0);           // 光
    
    Show();
    if(Status == Auto) {
        String Sensor="", Status="";
        
        /**  Lamp **/
        if(Light < safe_Light) {
          Sensor = "Lamp";
          Status = "on";
        } else {
          Sensor = "Lamp";
          Status = "off";
        }
        if( tempLamp != Sensor+Status) {
          SetController(Sensor, Status);
          tempLamp = Sensor+Status;
        }
        
        /**  Fan **/
        if(Temperature>safe_Temperature || Humidity>safe_Humidity) {
          Sensor = "Fan";
          Status = "on";
        } else {
          Sensor = "Fan";
          Status = "off";
        }
        if( tempFan != Sensor+Status) {
          SetController(Sensor, Status);
          tempFan = Sensor+Status;
        }
        
        /**  Sprinkler **/
        if(Soil < safe_Soil) {
          Sensor = "Sprinkler";
          Status = "on";
        } else {
          Sensor = "Sprinkler";
          Status = "off";
        }
        if( tempSprinkler != Sensor+Status) {
          SetController(Sensor, Status);
          tempSprinkler = Sensor+Status;
        }
    }
  }
  
  void SetController(String Sensor, String Status) {
    Serial.print("Sensor: ");
    Serial.print(Sensor);
    Serial.print(" ,Status: ");
    Serial.println(Status);
    
    if (client.connect(remote, 80)) {
      client.print("GET /ipps/api/equipment/");
      client.print(Sensor);
      client.print("/");
      client.print(Status);
      client.print("/");
      client.println(" HTTP/1.0");
      client.println();

      client.stop();
      client.flush();
    } else {
      Serial.println("connection failed");
    }
  }
  
  void Deliver() {
    if (client.connect(remote, 80)) {
      Serial.println("connected");
      client.print("GET /ipps/api/index/");
      client.print(Humidity);
      client.print("/");
      client.print(Light);
      client.print("/");
      client.print(Soil);
      client.print("/");
      client.print(Temperature);
      client.print("/");
      client.println(" HTTP/1.0");
      client.println();

      client.stop();
      client.flush();
      Serial.println("send!!");
    } else {
      Serial.println("connection failed");
    }
  }

  void Show() {
    Serial.println("-----------------------------------------");
    Serial.println("|                safety                 |");
    Serial.print("| Temperature: ");  // 溫度的值
    Serial.print(safe_Temperature); // 溫度的值
    Serial.print(", ");
    Serial.print("Humidity: ");     // 濕度的值
    Serial.println(safe_Humidity);  // 濕度的值
    Serial.print("| Light: ");
    Serial.print(safe_Light);
    Serial.print(", ");
    Serial.print("Soil: ");         // 土壤的值
    Serial.print(safe_Soil);        // 土壤的值
    Serial.print("% ");
    Serial.print("Status: ");       
    Serial.println(Status);           
    Serial.println("-----------------------------------------");
    Serial.print("| Temperature: ");  // 溫度的值
    Serial.print(Temperature);      // 溫度的值
    Serial.print(", ");
    Serial.print("Humidity: ");     // 濕度的值
    Serial.println(Humidity);        // 濕度的值
    Serial.print("| Light: ");
    Serial.print(Light);
    Serial.print(", ");
    Serial.print("Soil: ");         // 土壤的值
    Serial.print(Soil);             // 土壤的值
    Serial.println("%");
    Serial.println("=========================================");
    Serial.println("");
    Serial.println("");
  }
  
  void EthernetListen() {
    // Listen for incoming clients
    EthernetClient client = Listen.available();
    if (client) {
//      Serial.println("new client");
      // an http request ends with a blank line
      boolean currentLineIsBlank = true;
      while (client.connected()) {
        if (client.available()) {
          char c = client.read();
          Request += c;
          if (c == '\n' && currentLineIsBlank) {
            Control(Request);
            //Serial.println(Request);
            Request = "";
            break;
          }
          if (c == '\n') {
            // you're starting a new line
            currentLineIsBlank = true;
          } else if (c != '\r') {
            // you've gotten a character on the current line
            currentLineIsBlank = false;
          }
        }
      }
      client.stop();
      //Serial.println("client disonnected");
    }
  }
  
  
  /* network url control - http://IP/XXX */
  void Control(String Request) {
    
    if(Request.indexOf("/safetys") > -1) {
      String safetys = Request.substring(Request.indexOf("[")+1, Request.indexOf("]"));
      safety_split(safetys);
    } else if(Request.indexOf("/Status=Auto") > -1) {  
      Status = Auto;
    } else if(Request.indexOf("/Status=Manually") > -1) {  
      Status = Manually;
    } else if(Request.indexOf("/Lamp=on") > -1) {
      digitalWrite(9, HIGH);
      tempLamp = "Lampon";
    } else if(Request.indexOf("/Lamp=off") > -1) {
      digitalWrite(9, LOW);
      tempLamp = "Lampoff";
    } else if(Request.indexOf("/Fan=on") > -1) {
      digitalWrite(2, HIGH);
      tempFan = "Fanon";
    } else if(Request.indexOf("/Fan=off") > -1) {
      digitalWrite(2, LOW);
      tempFan = "Fanoff";
    } else if(Request.indexOf("/Sprinkler=on") > -1) {
      digitalWrite(3, HIGH);
      delay(5000);
      digitalWrite(3, LOW);
      delay(3000);
      tempSprinkler = "Sprinkleron";
      SetController("Sprinkler", "off");
    } else if(Request.indexOf("/Sprinkler=off") > -1) {
      digitalWrite(3, LOW);
      tempSprinkler = "Sprinkleroff";
    }
  }
  
  /* https://www.inkling.com/read/arduino-cookbook-michael-margolis-2nd/chapter-2/recipe-2-7 */
  void safety_split(String request) {
    int i=0;
    int commaPosition;
    String value = "";
    do {
      commaPosition = request.indexOf(',');
      if(commaPosition != -1) {
        value = request.substring(0, commaPosition);
        request = request.substring(commaPosition+1, request.length());
      } else {  // here after the last comma is found
        //Serial.println(request);  // if there is text after the last comma,
        value = request;
      }
      if(value.length() > 0) {
        switch(i) {
          case 0:
            safe_Humidity = value.toInt();
            break;
          case 1:
            safe_Light = value.toInt();
            break;
          case 2:
            safe_Soil = value.toInt();
            break;
          case 3:
            safe_Temperature = value.toInt();
            break;
        } 
      }
      i++;
    } while(commaPosition >=0);
  }
  
