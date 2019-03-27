# placid-platypus
![](https://img.shields.io/badge/build-passing-brightgreen.svg)  ![](https://img.shields.io/badge/deployed-true-brightgreen.svg)  
PlacidPlatypus is an open source text-based assistant, that can be installed on Linux, Raspberry Pi and other platforms. Our opensource skills are written in Java. Come join us!

## getting started

First, get the code on your system!  The simplest method is via git:
- `cd ~/`
- `git clone https://github.com/jorisschellekens/placid-platypus.git`
- `cd placid-platypus`

## core concepts

- skill : A piece of code that is capable of handling a particular input (or a particular set of inputs). A skill can provide immediate output, or choose to manipulate the input (potentially allowing other skills to provide output).
- utterance : A piece of text that triggers the execution of a skill.

## differences with Alexa / Google Home / etc

Most (if not all) existing (conversational) AI platforms follow the same approach.
They focus on splitting the entire input domain (all phrases that could be uttered, typed, etc) into discrete subdomains.
Then a number of 'skills' are implemented in such a way that each skill becomes uniquely responsible for a particular subdomain.

e.g. 
- Asking Alexa "Count to 5" will force Alexa to respond "1..2..3..4..5"
- Asking Alexa "How much is 5+5" will have her respond "10" 

The problem is that human intelligence typically doesn't work like that. Knowledge builds on top of other knowledge.
That's how this AI works.

A skill **can** choose to immediately present its output to the system. But a skill can also perform a redirect.
Essentially saying "The Math skill doesn't know how to respond to this query. But it does know how to change the query."

A lot of the implementation details are not mentioned here (how to handle multiple, concurrent interpretations for instance).
But those can be observed from the code.

## skills

| Skill         | Explanation   | Example Utterance  |
| ------------- | ------------- | :----------------: |
| AIML          | AIML allows the bot to perform some basic synonymy, grammatical corrections, or respond to input that will always require the same output. | 'Hi' |
| BlackJack      | This skill allows the bot to play blackjack.      |   'I want to play Blackjack.' |
| BotStatistics | This skill aggregates statistics from the bot itself, and the conversations it has had.      |    'How many people have you talked to?' |
| Cocktail | This skill allows the bot to give cocktail-advice. | 'How do you mix a mojito?' |
| DuckDuckGo | This skill handles generic 'who is .. ?' queries. | 'Who is Michael Jackson?' |
| Hangman | This skill allows the bot to play hangman. | 'Let's play hangman.' |
| IMDB | This skill allows the bot to give movie advice/facts. | 'Do you know the movie Mrs Doubtfire?' | 
| Math | This skill allows the bot to handle (simple) mathematics. | 'How much is five plus 8?' |
| NYTimes | | 
| Quote | |
| SemanticMatch | This skill learns from past input/output pairs, allowing new semantically similar (word2vec) input to be matched to previous (known correct) input. | 'Show me a cat please' |
| SoundCloud | |
| TicTacToe | This skill allows the bot to play tic tac toe.| 'I want to play tic tac toe.' |
| Time | This skill allows the bot to provide answers to date/time queries. | 'What time is it?' |
| TypoCorrection | This skill learns from past input/outputs pairs, allowing new input to still be matched correctly despite typos. | 'Who is Mihaek Jackson?' |
| Unsplash | This skill allows the user to search for images on Unsplash. | 'Do you have a picture of a cat?' |
| Weather | This skill allows the user to query the bot for weather information. | 'What's the weather like in Antwerp?' |
| Xkcd | This skill allows the user to check the latest (and other) XKCD comments. Which is awesome. | 'Show XKCD nr 666'

## getting involved

This is an open source project and we would love your help.

If this is your first PR or you're not sure where to get started, say hi and a team member would be happy to mentor you.

