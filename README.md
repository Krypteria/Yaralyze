# Yaralyze

<div align="justify">

There are currently more than **7,260,000,000,000 mobile devices in the world**, which means that **91.54% of the world's population has one**. Approximately **2,500,000,000,000 of these devices have Android** as their operating system.

It is no secret that these devices are becoming more and more important to us, they are with us practically all day long and contain a lot of personal information, which makes them an interesting target for malicious actors. 

</div>

## ¿How malware can be detected?

<div align="justify">

Malware analysis can be categorised into 3 main types. **static analysis**, **dynamic analysis** and **hybrid analysis**.

Static analysis is considered to be any analysis that does not have to execute the code to analyse it, it is based on the search for patterns through rules or heuristics which makes them extremely safe because there is no possibility of activating the malware unintentionally.  This type of analysis is faster than dynamic analysis and has a high detection rate for known malware by the very nature of its detection system.

Dynamic scanning, on the other hand, is any scan that needs to run the malware to analyse it, which means that a larger infrastructure must be in place to isolate it so that its execution does not affect real systems. This type of scanning is more reliable than static scanning and can detect unknown malware.

Finally, an analysis that uses both static and dynamic analysis techniques is known as hybrid analysis. Currently, well-known anti-malware solutions such as Kaspersky, Avira or Avast, among others, use this type of analysis, dividing it into distinct stages.

</div> 

## ¿What is a Yara Rules?

<div align="justify">

Within the category of static analysis are the Yara rules. Yara rules are a type of malware signature that allows to identify and classify known malware.

<img src=https://user-images.githubusercontent.com/55555187/201518699-f1d23ece-2574-478b-92db-99106f156ef5.png><br>

The rules have three sections, a meta section where information about the rule itself is usually placed, the strings section where the patterns on which we are going to compare the malware are defined and the conditions section where the condition that the pattern must meet for the file to be considered malware is defined. The yara rules can be extremely complex, so I recommend reading their [documentation](https://yara.readthedocs.io/en/stable/) if you want to understand in more detail how they work. 

Another favourable point of yara rules is that they are a current technique that is starting to be widely used by analysts, which means that there are a large number of contributions.

</div>

## Now lets go to the point: Yaralyze, malware detection tool

<div align="justify">

Yaralyze is a malware detection tool that **employs two static analysis techniques**, one using **yara rules** and the other based on **hashes analysis**. It allows the **storage and visualisation of reports**, it is designed using a **client-server architecture** where **the server can be hosted in the cloud** so that it is always available from any mobile device that has the client installed and makes use of **+130,000 Yara rules and +500,000 hashes of malware apps** obtained from virusShare and Github (the rules and hashes are not published in the repository).

</div>

<p float="left">
  <img src="https://user-images.githubusercontent.com/55555187/201519071-025a0338-2508-4cdb-b908-5072c8f80468.png">
  <img src="https://user-images.githubusercontent.com/55555187/201519069-91fa008b-c1cf-4a54-88d3-10db258927e6.png">
  <img src="https://user-images.githubusercontent.com/55555187/201519070-9b67fea3-013c-4014-a60b-0566ba9f367e.png">
</p>

### Analysis with Yara Rules
<img src="https://user-images.githubusercontent.com/55555187/201520917-5321d4a4-dc7f-49c2-9132-bd14a1274c97.png">

### Analysis of application hashes
<img src="https://user-images.githubusercontent.com/55555187/201520916-a51bf4b6-bb3e-4d79-8bd9-4dded430c89b.png">

## Testing on real malware

<div align="justify">

Two types of tests were carried out. One type of test consisted of testing the effectiveness of the tool in detecting known malware, using samples of **Brata**, **Sharkbot**, **Cerberus** and **Flubot** malwares, and the other was to test the speed of analysis.

![analysis2](https://user-images.githubusercontent.com/55555187/201522191-a189cddd-cc55-48da-b844-5c4aadd6fa79.png)
![analysis1](https://user-images.githubusercontent.com/55555187/201522194-c617994b-d689-4724-9ad2-f459e012549e.png)


As it can be seen in the images, it manages to detect the malware files and does not produce false positives with the real APK of winrar.


| APK                | T1   |T2    |T3    |T4    | Average| 
|--------------------|------|------|------|------|--------|
| Flubot (malware)   |2.27s |2.23s |2.24s |2.29s |2.257s  |
| Sharkbot (malware) |2.54s |2.51s |2.53s |2.56s |2.535s  | 
| Winrar             |2.18s |2.20s |2.16s |2.16s |2.175s  | 

| Location of the application hash                | T1   |T2    |T3    |T4    | Average|
|--------------------|------|------|------|------|--------|
| Client DB   |0.079s |0.081s |0.078s |0.077s |0.0787s  |
| Server DB |0.088s |0.085s |0.087s |0.091s |0.0877s  | 
| No coincidence             |0.087s |0.088s |0.084s |0.088s |0.0867s  | 

</div>
