#include <Ethernet.h>
#include <SPI.h>
#include <SHT1x.h>

  #define dataPin  6
  #define clockPin 7
  
  byte mac[] = {0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED}; // 乙太網路
  IPAddress ip(192, 168, 0, 30);                     // local IP
  IPAddress remote(192, 168, 0, 130);                // remote IP
  EthernetServer Listen(80);                         // server
  EthernetClient client;                             // client
  
  SHT1x sht1x(dataPin, clockPin);                    // 傳感器
  
  boolean EthernetTf = true;
  String Request;                                    // stores the HTTP request
  float Temperature, Humidity;                       // 溫度、濕度
  int Soil, Light;                                   // 土壤, 光
  
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
  }
  
  void loop() {
    int Send   = analogRead(A10);           // 光
    int Listen = analogRead(A11);           // 光
    
    if(Send < 10 ) {
      //EthernetSend();
    }
    
    if(Listen < 10) {
     //EthernetListen();
    } 
    EthernetListen();
    Serial.print("Send: ");
    Serial.print(Send);
    Serial.print("  Listen: ");
    Serial.println(Listen);
    delay(1000);
  }
  
  void EthernetSend() {
    Temperature = sht1x.readTemperatureC(); // 溫度傳感器
    Humidity    = sht1x.readHumidity();     // 濕度傳感器
    int loo     = analogRead(A1);
    Soil        = (1000-(loo-23))/10;       // 土壤
    Light       = analogRead(A0);           // 光
    
    if(time == 5) { 
      Deliver();
      Show();
      time = 0;
    }
    time++;
  }
  
  void Deliver() {
    Serial.println("connecting...");
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
    Serial.print("Light: ");
    Serial.println(Light);
    Serial.print("Temperature: ");  // 溫度的值
    Serial.print(Temperature);      // 溫度的值
    Serial.print(",");
    Serial.print("Humidity: ");     // 濕度的值
    Serial.println(Humidity);       // 濕度的值
    Serial.print("Soil: ");         // 土壤的值
    Serial.print(Soil);             // 土壤的值
    Serial.println("%");
    Serial.println("=========================================");
  }
  
  void EthernetListen() {
    // Listen for incoming clients
    EthernetClient client = Listen.available();
    if (client) {
      Serial.println("new client");
      // an http request ends with a blank line
      boolean currentLineIsBlank = true;
      while (client.connected()) {
        if (client.available()) {
          char c = client.read();
          Request += c;
          if (c == '\n' && currentLineIsBlank) {
            Control(Request);
            Serial.println(Request);
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
      Serial.println("client disonnected");
    }
  }
  
  void Control(String Request) {
    if(Request.indexOf("/Light=on") > -1) {
      digitalWrite(9, HIGH);
    } else if(Request.indexOf("/Light=off") > -1) {
      digitalWrite(9, LOW);
    } else if(Request.indexOf("/Fan=on") > -1) {
      digitalWrite(2, HIGH);
    } else if(Request.indexOf("/Fan=off") > -1) {
      digitalWrite(2, LOW);
    } else if(Request.indexOf("/Pump=on") > -1) {
      digitalWrite(3, HIGH);
      delay(5000);
      digitalWrite(3, LOW);
      delay(3000);
    } else if(Request.indexOf("/Pump=off") > -1) {
      digitalWrite(3, LOW);
    }
  }
