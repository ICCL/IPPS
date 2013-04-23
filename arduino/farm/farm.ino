#include <Ethernet.h>
#include <SPI.h>
#include <SHT1x.h>

  #define dataPin  6
  #define clockPin 7
  byte mac[] = {0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED}; // 乙太網路
  IPAddress server(192, 168, 0, 104);                // IP
  SHT1x sht1x(dataPin, clockPin);                    // 傳感器
  EthernetClient client;                             // 建立客戶端可以連接到指定的網絡的IP地址
  float Temperature, Humidity;                       // 溫度、濕度
  int Soil, Light;                                   // 土壤, 光
  boolean EthernetTf = true;

  int time = 0;

  void setup() {
    Serial.begin(9600);
    pinMode(A0, INPUT);             // 設定腳位位置的行為
    pinMode(9, OUTPUT);             // 同上
    pinMode(2, OUTPUT);             // 同上
    pinMode(3, OUTPUT);             // 同上


    Serial.println("begin Ethernet..");
    if(!verifyEthernet()) {
      EthernetTf = false;
      Serial.println("Failed to configure Ethernet using DHCP"); // 乙太網路連線失敗
    }
  }

  boolean verifyEthernet() {
    if (Ethernet.begin(mac) == 0)
      return false;
    else
      return true; 
    }

  void loop() {
    if(EthernetTf) {
      Temperature = sht1x.readTemperatureC(); // 溫度傳感器
      Humidity    = sht1x.readHumidity();     // 濕度傳感器
      int loo     = analogRead(A1);
      Soil        = (1000-(loo-23))/10;       // 土壤
      Light       = analogRead(A0);           // 光

      /** 光的值小於299，就亮燈 **/
//		    if(Light<299) { 
//		    	  digitalWrite(9, HIGH);
//		    } else {
//			  digitalWrite(9, LOW);
//		    }

                    /** 溫度大於26度和濕度大於69，開風散 **/
//		    if(Temperature>26|Humidity>69) { 
//			  digitalWrite(2, HIGH);
//		    } else {
//			  digitalWrite(2, LOW);
//		    }

                    /** 土壤濕度小於19，開起灑水 **/
//		    if(Soil<19) { 
//			  digitalWrite(3, HIGH);
//			  delay(5000);
//			  digitalWrite(3, LOW);
//			  delay(3000);
//		    } else {
//			  digitalWrite(3, LOW);
//		    }

                    /** 設定資料上傳間隔時間 time==這裡以秒為單位 **/
      if(time==5) { 
        Deliver();
      }
      time++;
      Show();
    } else {
      if(!verifyEthernet()) {
        EthernetTf = false;
        Serial.println("Failed to configure Ethernet using DHCP"); // 乙太網路連線失敗
      } else {
        EthernetTf = true;
      }
    }
    delay(1000);
  }

  void Deliver() {
    Serial.println("connecting...");
    if (client.connect(server, 80)) {
      Serial.println("connected");
      client.print("GET /ipps/api/index/");
      client.print(Temperature);
      client.print("/");
      client.print(Humidity);
      client.print("/");
      client.print(Soil);
      client.print("/");
      client.print(Light);
      client.print("/");
      client.println(" HTTP/1.0");
      client.println();

      client.stop();
      client.flush();
      Serial.println("send!!");
    } else {
      Serial.println("connection failed");
    }
    time = 0;
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

