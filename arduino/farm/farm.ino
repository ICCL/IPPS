#include <SPI.h>   

#include <SHT1x.h>  

#include <Ethernet.h>  

byte mac[] = {  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED }; //乙太網路

IPAddress server(192,168,0,110); 	//IP

#define dataPin  6 

#define clockPin 7  

int time = 0;

void setup() {

Serial.begin(9600);

pinMode(A0, INPUT);  	//設定腳位位置的行為

pinMode(9, OUTPUT);  	//同上

pinMode(2, OUTPUT);	//同上

pinMode(3, OUTPUT);	//同上

SHT1x sht1x(dataPin, clockPin);  	//傳感器

EthernetClient client;  		//建立客戶端可以連接到指定的網絡的IP地址

int SoilPin = A1; 			//土壤

int lol = 0;  				

int Light = analogRead(A0);		//光

lol = analogRead(A1);  			//溫濕度

int Soil = (1000-(lol-23))/10;		

float Temperature, Humidity; 		//溫度、濕度

Temperature = sht1x.readTemperatureC(); //溫度傳感器

Humidity = sht1x.readHumidity(); 	//濕度傳感器

while (!Serial) {   

    ; 

}

  if (Ethernet.begin(mac) == 0) {  

    Serial.println("Failed to configure Ethernet using DHCP");	//乙太網路連線失敗

    for(;;)

      ;

  }

  delay(1000);

  Serial.println("connecting...");		//連接中

  if (client.connect(server, 80)) {  		//客戶端連接

    Serial.println("connected");		//連接

    Serial.println(Light);				//光的值

    client.print("GET /plant/sensor.php?Light=");  	//這應該是跟PHP連的

    client.print(Light);				//打印連接到伺服器的客戶端的數據

    client.print("&Temperature=");		//溫度

    client.print(Temperature);			

    client.print("&Humidity=");			//濕度

    client.print(Humidity);				

    client.print("&Soil=");			//土壤

    client.print(Soil);					

    client.println(" HTTP/1.0");

    client.println();

  Serial.print(Temperature);			//溫度的值

  Serial.print(",");

  Serial.println(Humidity);			//濕度的值

  Serial.print(Soil);				//土壤的值

  Serial.println("%");

  } 

   else {

    Serial.println("connection failed");	//連接失敗

  }

}



void loop()

{

SHT1x sht1x(dataPin, clockPin);

int Te = sht1x.readTemperatureC();

int hum = sht1x.readHumidity();

int loo = analogRead(A1);    

int Soil = (1000-(loo-23))/10;

int Light = analogRead(A0);

if(Light<299)    			//光的值小於299，就亮燈

{

digitalWrite(9, HIGH);

}

else{

digitalWrite(9, LOW);

}

if(Te>26|hum>69)   			//溫度大於26度和濕度大於69，開風散

{

digitalWrite(2, HIGH);

}

else{

digitalWrite(2, LOW);

}

if(Soil<19)   				//土壤濕度小於19，開起灑水

{

digitalWrite(3, HIGH);

delay(5000);

digitalWrite(3, LOW);

delay(3000);

}

else{

digitalWrite(3, LOW);

}

if(time==1800)     //設定資料上傳間隔時間 time==這裡以秒為單位

{

Deliver();

}

delay(1000);

time++;

}

void Deliver()  

{

SHT1x sht1x(dataPin, clockPin);

EthernetClient client;

int Soil = 0;

int Light = analogRead(A0);

int olo = analogRead(A1);  

int So = (1000-(olo-23))/10;

float Temperature, Humidity;

Temperature = sht1x.readTemperatureC();

Humidity = sht1x.readHumidity();

while (!Serial) {

    ; 

}

  if (Ethernet.begin(mac) == 0) {

    Serial.println("Failed to configure Ethernet using DHCP");

    for(;;)

      ;

  }

  delay(1000);

  Serial.println("connecting...");



  if (client.connect(server, 80)) {

    Serial.println("connected");

    Serial.println(Light);

    client.print("GET /plant/sensor.php?Light=");

    client.print(Light);

    client.print("&Temperature=");

    client.print(Temperature);

    client.print("&Humidity=");

    client.print(Humidity);

    client.print("&Soil=");

    client.print(Soil);

    client.println(" HTTP/1.0");

    client.println();

  Serial.print(Temperature);

  Serial.print(",");

  Serial.println(Humidity);

  Serial.println(So);

 time = 0;

  } 

   else {

    Serial.println("connection failed");

  }

 }


