# Watson Developer Cloud Java Wrapper
[![Build Status](https://secure.travis-ci.org/watson-developer-cloud/java-wrapper.png)](http://travis-ci.org/watson-developer-cloud/java-wrapper)  
Java code wrappers to quickly get started with the various [Watson Developer Cloud][wdc] services - A collection of REST APIs and SDKs that use cognitive computing to solve complex problems.

## Table of Contents
  * [Watson Developer Cloud][wdc]
    * [Questions](#questions)
    * [Installation](#installation)
      * [Maven](#maven)
      * [Gradle](#gradle)
    * [Usage](#usage)
    * [Getting the Service Credentials](#getting-the-service-credentials)
    * [IBM Watson Services](#ibm-watson-services)
      * [Concept Expansion](#concept-expansion)
      * [Language Identification](#language-identification)
      * [Language Translation](#language-translation)
      * [Machine Translation](#machine-translation)
      * [Message Resonance](#message-resonance)
      * [Natural Language Classifier](#natural-language-classifier)
      * [Personality Insights](#personality-insights)
      * [Question and Answer](#question-and-answer)
      * [Relationship Extraction](#relationship-extraction)
      * [Speech to Text](#speech-to-text)
      * [Text to Speech](#text-to-speech)
      * [Tradeoff Analytics](#tradeoff-analytics)
      * [Visual Recognition](#visual-recognition)
    * [Running in Bluemix](#running-in-bluemix)
    * [Debug](#debug)
    * [Tests](#tests)
    * [Open Source @ IBM](#open-source--ibm)
    * [License](#license)
    * [Contributing](#contributing)

## Questions

If you are having difficulties using the APIs or have a question about the IBM
Watson Services, please ask a question on
[dW Answers](https://developer.ibm.com/answers/questions/ask/?topics=watson)
or [Stack Overflow](http://stackoverflow.com/questions/ask?tags=ibm-watson).

## Installation

Download the [jar][releases], and add it to your project.  
Now, you are ready to see some [examples](https://github.com/watson-developer-cloud/java-wrapper/tree/master/src/main/examples/java/com/ibm/watson/developer_cloud).

##### Maven
```xml
<dependency>
	<groupId>com.ibm.watson.developer_cloud</groupId>
	<artifactId>java-wrapper</artifactId>
	<version>0.1.9</version>
</dependency>
```
##### Gradle

```gradle
'com.ibm.watson.developer_cloud:java-wrapper:0.1.9'
```

## Usage

The examples below assumes that you already have service credentials. If you don't have service credentials,
you will have to create and bind the service in [Bluemix][bluemix].

If you are running your application in Bluemix, you don't need to specify the
credentials; the wrapper will get them for you by looking at the `VCAP_SERVICES`
environment variable.

### Getting the Service Credentials

The credentials for the services are stored in the
[VCAP_SERVICES][vcap_environment] environment variable. To get them, you need
to first create and bind the service to your application.

You will need the `username` and `password` credentials for each service these are *not* your Bluemix credentials, and are found in the VCAP_SERVICES variable on Bluemix, and they are different for each service.

## IBM Watson Services
The Watson Developer Cloud offers a variety of services for building cognitive
applications.

### Concept Expansion
Map euphemisms or colloquial terms to more commonly understood phrases using
the [Concept Expansion][concept_expansion] service.
Example: Create a job, wait for it to finish, and then retrieve results.

```java
import com.ibm.watson.developer_cloud.concept_expansion.v1.ConceptExpansion;
import com.ibm.watson.developer_cloud.concept_expansion.v1.model.ConceptExpansionDataset;
import com.ibm.watson.developer_cloud.concept_expansion.v1.model.Job;

ConceptExpansion service = new ConceptExpansion();
service.setUsernameAndPassword("<username>", "<password>");

String[] seeds = new String[]{"motrin","tylenol","aspirin"};
String label = "medicine";
service.setDataset(ConceptExpansionDataset.MT_SAMPLES);
Job job = service.createJob(label, seeds);

while (service.getJobStatus(job) == Job.Status.AWAITING_WORK
		|| service.getJobStatus(job) == Job.Status.IN_FLIGHT) {
	try {
		Thread.sleep(4000);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
}

System.out.println(service.getJobResult(job));
```


### Language Identification
Example: Identify a language using the [Language Identification][language_identification] service.

```java
import com.ibm.watson.developer_cloud.language_identification.v1.LanguageIdentification;
import com.ibm.watson.developer_cloud.language_identification.v1.model.IdentifiedLanguage;

LanguageIdentification service = new LanguageIdentification();
service.setUsernameAndPassword("<username>", "<password>");

IdentifiedLanguage lang = service.identify("The language identification service takes text input and identifies the language used.");
System.out.println(lang);
```

### Language Translation
Select a domain, then identify or select the language of text, and then translate the text from one supported language to another.

Example: Translate 'hello' from English to Spanish using the [Language Translation][language_translation] service.

```java
import com.ibm.watson.developer_cloud.language_translation.v2.LanguageTranslation;
import com.ibm.watson.developer_cloud.language_translation.v2.model.TranslationResult;

LanguageTranslation service = new LanguageTranslation();
service.setUsernameAndPassword("<username>", "<password>");

TranslationResult translationResult = service.translate("hello", "en", "es");
System.out.println(translationResult);
```

### Machine Translation
Example: Translate text from one language to another using the [Machine Translation][machine_translation] service.

```java
import com.ibm.watson.developer_cloud.machine_translation.v1.MachineTranslation;
import com.ibm.watson.developer_cloud.machine_translation.v1.model.Language;

MachineTranslation service = new MachineTranslation();
service.setUsernameAndPassword("<username>", "<password>");
String response = service.translate("The IBM Watson team is awesome", Language.ENGLISH, Language.SPANISH);
System.out.println(response);
```

### Message Resonance
Send a word and Watson responds with the resonance scoring for the word, allowing you to enhance the effectiveness of your language for the intended audience.

Example: Get resonance information for individual words in a sentence from the [Message Resonance][message_resonance] service.

```java
import com.ibm.watson.developer_cloud.message_resonance.v1.MessageResonance;
import com.ibm.watson.developer_cloud.message_resonance.v1.model.MessageResonanceDataset;
import com.ibm.watson.developer_cloud.message_resonance.v1.model.Message;

MessageResonance service = new MessageResonance();
service.setUsernameAndPassword("<username>", "<password>");

service.setDataset(MessageResonanceDataset.BIG_DATA);
Message message = service.getResonance("IBM Watson Developer Cloud");
System.out.println(message);
```

### Natural Language Classifier

Use [Natural Language Classifier](http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/nl-classifier/) service to create a classifier instance by providing a set of representative strings and a set of one or more correct classes for each as training. Then use the trained classifier to classify your new question for best matching answers or to retrieve next actions for your application.

```java
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;

NaturalLanguageClassifier service = new NaturalLanguageClassifier();
service.setUsernameAndPassword("<username>", "<password>");

Classification classification = service.classify("<classifier-id>", "Is it sunny?");

System.out.println(classification);
```

**Note:** You will need to create and train a classifier in order to be able to classify phrases.


### Personality Insights
Use linguistic analytics to infer personality and social characteristics, including Big Five, Needs, and Values, from text. 

Example: Analyze text and get a personality profile using the [Personality Insights][personality_insights] service.

```java
import com.ibm.watson.developer_cloud.personality_insights.v2.PersonalityInsights;
import com.ibm.watson.developer_cloud.personality_insights.v2.model.Profile;

PersonalityInsights service = new PersonalityInsights();
service.setUsernameAndPassword("<username>", "<password>");

// Demo content from Moby Dick by Hermann Melville (Chapter 1)
String text = "Call me Ishmael. Some years ago-never mind how long precisely-having "
    + "little or no money in my purse, and nothing particular to interest me on shore, "
    + "I thought I would sail about a little and see the watery part of the world. "
    + "It is a way I have of driving off the spleen and regulating the circulation. "
    + "Whenever I find myself growing grim about the mouth; whenever it is a damp, "
    + "drizzly November in my soul; whenever I find myself involuntarily pausing before "
    + "coffin warehouses, and bringing up the rear of every funeral I meet; and especially "
    + "whenever my hypos get such an upper hand of me, that it requires a strong moral "
    + "principle to prevent me from deliberately stepping into the street, and methodically "
    + "knocking people's hats off-then, I account it high time to get to sea as soon as I can. "
    + "This is my substitute for pistol and ball. With a philosophical flourish Cato throws himself "
    + "upon his sword; I quietly take to the ship. There is nothing surprising in this. "
    + "If they but knew it, almost all men in their degree, some time or other, cherish "
    + "very nearly the same feelings towards the ocean with me. There now is your insular "
    + "city of the Manhattoes, belted round by wharves as Indian isles by coral reefs-commerce surrounds "
    + "it with her surf. Right and left, the streets take you waterward.";

Profile profile = service.getProfile(text);
System.out.println(profile);
```
**Node:** Don't forget to update the `text` variable! Also, if you experience
authentication errors, remember that the Personality Insights service is not
a free service.

### Question and Answer
Ask a healthcare-related question of the
[Question and Answer][question_and_answer] service.

```java
import com.ibm.watson.developer_cloud.question_and_answer.v1.QuestionAndAnswer;
import com.ibm.watson.developer_cloud.question_and_answer.v1.model.Pipeline;
import com.ibm.watson.developer_cloud.question_and_answer.v1.model.QuestionAndAnswerDataset;


QuestionAndAnswer service = new QuestionAndAnswer();
service.setUsernameAndPassword("<username>", "<password>");

service.setDataset(QuestionAndAnswerDataset.HEALTHCARE);
WatsonAnswer watsonAnswers = service.ask("What is HIV?");

System.out.println(watsonAnswers);
```

### Relationship Extraction
Analyze an English news article and get the relationships between sentence
components (nouns, verbs, subjects, objects, etc.) by using the
[Relationship Extraction][relationship_extraction] service.

```java
import com.ibm.watson.developer_cloud.relationship_extraction.v1.RelationshipExtraction;
import com.ibm.watson.developer_cloud.relationship_extraction.v1.model.RelationshipExtractionDataset;

RelationshipExtraction service = new RelationshipExtraction();
service.setUsernameAndPassword("<username>", "<password>");

service.setDataset(RelationshipExtractionDataset.ENGLISH_NEWS);
String response = service.extract("IBM Watson Developer Cloud");
System.out.println(response);
```

### Speech to Text
Use the [Speech to Text][speech_to_text] service to recognize the text from a
.wav file.

```java
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import java.io.File;

SpeechToText service = new SpeechToText();
service.setUsernameAndPassword("<username>", "<password>");

File audio = new File("sample1.wav");

SpeechResults transcript = service.recognize(audio, "audio/l16; rate=44100");
System.out.println(transcript);
```

#### WebSocket support

Speech to Text supports WebSocket so you can use a websocket client like the one in: http://java-websocket.org/
The websocket url is: `wss://stream.watsonplatform.net/speech-to-text/api/v1/recognize`

### Text to Speech
Use the [Text to Speech][text_to_speech] service to get the available voices to synthesize.

```java
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

TextToSpeech service = new TextToSpeech();
service.setUsernameAndPassword("<username>", "<password>");

List<Voice> voices = service.getVoices();
System.out.println(voices);
```

### Tradeoff Analytics
Use the [Tradeoff Analytics][tradeoff_analytics] service to find the best
phone that minimizes price and weight and maximizes screen size.

```java
import com.ibm.watson.developer_cloud.tradeoff_analytics.v1.TradeoffAnalytics;
import com.ibm.watson.developer_cloud.tradeoff_analytics.v1.model.Dilemma;
import com.ibm.watson.developer_cloud.tradeoff_analytics.v1.model.Problem;

TradeoffAnalytics service = new TradeoffAnalytics();
service.setUsernameAndPassword("<username>", "<password>");

Problem problem = new Problem("phone");

// Define the objectives
List<Column> columns = new ArrayList<Column>();
problem.setColumns(columns);
columns.add(new Column("price", "Price", "NUMERIC", true, "MIN"));
columns.add(new Column("ram", "Ram", "NUMERIC", false, "MAX"));
columns.add(new Column("screen", "Screen", "NUMERIC", true, "MAX"));

// Define the options to choose
List<Option> options = new ArrayList<Option>();
problem.setOptions(options);

HashMap<String, String> galaxySpecs = new HashMap<String, String>();
galaxySpecs.put("price", "300");
galaxySpecs.put("ram", "45");
galaxySpecs.put("screen", "5");
options.add(new Option("1", "Galaxy S4", galaxySpecs, ""));

HashMap<String, String> iphoneSpecs = new HashMap<String, String>();
iphoneSpecs.put("price", "400");
iphoneSpecs.put("ram", "40");
iphoneSpecs.put("screen", "4");
options.add(new Option("2", "iPhone 5", iphoneSpecs, ""));

HashMap<String, String> optimusSpecs = new HashMap<String, String>();
optimusSpecs.put("price", "300");
optimusSpecs.put("ram", "300");
optimusSpecs.put("screen", "5");
options.add(new Option("3", "LG Optimus G", optimusSpecs, ""));

// Call the service and get the resolution
Dilemma dilemma = service.dilemmas(problem);
System.out.println(dilemma);
```

### Visual Recognition
Use the [Visual Recognition][visual_recognition] service to recognize the
following picture.

![Car](http://visual-recognition-demo.mybluemix.net/images/73388.jpg)

```java
import com.ibm.watson.developer_cloud.visual_recognition.v1.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v1.model.RecognizedImage;
import com.ibm.watson.developer_cloud.visual_recognition.v1.model.LabelSet;
import java.io.File;

VisualRecognition service = new VisualRecognition();
service.setUsernameAndPassword("<username>", "<password>");

File image = new File("car.png");

LabelSet labelSet = new LabelSet();
labelSet.withLabelGroup("Animal").withLabelGroup("Food");

RecognizedImage recognizedImage = service.recognize(image, labelSet);
System.out.println(recognizedImage);
```

## Android

The wrapper works well on Android. It depends on
[Apache HttpClient][] (including the [HttpMime][] module) and [json-java][].
Android SDK already comes with these two libraries so you don't need to include
them when using the wrapper there.


## Build + Test

To build and test the project you can use [Gradle][] (version 1.x): or [Apache Maven][apache_maven].

Gradle:

  ```sh
  $ cd java-wrapper
  $ gradle jar  # build jar file (build/libs/watson-developer-cloud-0.1.8.jar)
  $ gradle test # run tests
  ```

or Maven:

  ```sh
  $ cd java-wrapper
  $ mvn install
  ```

## Working with Eclipse and Intellij IDEA

If you want to work on the code in an IDE instead of a text editor you can
easily create project files with gradle:

  ```sh
  $ gradle idea     # Intellij IDEA
  $ gradle eclipse  # Eclipse
  ```

or maven:

  ```sh
  $ mvn idea:idea # Intellij IDEA
  $ mvn eclipse:eclipse # Eclipse
  ```


## Open Source @ IBM
Find more open source projects on the [IBM Github Page](http://ibm.github.io/)

## License

This library is licensed under Apache 2.0. Full license text is
available in [LICENSE](LICENSE).

## Contributing
See [CONTRIBUTING.md](CONTRIBUTING.md).


[question_and_answer]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/qaapi/
[message_resonance]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/mrapi/
[personality_insights]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/personality-insights/
[language_identification]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/lidapi/
[machine_translation]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/mtapi/
[concept_expansion]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/glimpseapi/
[relationship_extraction]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/sireapi/
[language_translation]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/language-translation/
[visual_recognition]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/visual-recognition/
[tradeoff_analytics]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/tradeoff-analytics/
[text_to_speech]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/text-to-speech/
[speech_to_text]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/speech-to-text/
[wdc]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/
[vcap_environment]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/getting_started/index.html#EnvVars
[bluemix]: https://console.ng.bluemix.net
[Gradle]: http://www.gradle.org/
[Apache HttpClient]: http://hc.apache.org/httpcomponents-client-ga/
[HttpMime]: http://hc.apache.org/httpcomponents-client-ga/httpmime
[json-java]: http://json.org/java/
[apache_maven]: http://maven.apache.org/
[releases]: https://github.com/watson-developer-cloud/java-wrapper/releases
