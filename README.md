
# Skript-GPT

A Skript Addon that allows users to interface with the OpenAI API and lets them send completion requests.

[Documentation](https://skripthub.net/docs/?addon=)


## Documentation

[![SkriptHubViewTheDocs](http://skripthub.net/static/addon/ViewTheDocsButton.png)](http://skripthub.net/docs/?addon=Skript-GPT)

## Effects

### GPT Chat Completion Request
```
(generate|make) [a] chat[gpt] completion with (prompt|input) %string% [and model %-string%] [and max tokens %-number%] [and temperature %-number%]
(generate|make) [a] chat[gpt] completion with conversation %conversationmessages% [and model %-string%] [and max tokens %-number%] [and temperature %-number%]
```

Generate a response to a prompt string using the OpenAI ChatGPT API, based on an input prompt.

<details>
	<summary>GPT Chat Completion WITH Conversation Persistence</summary>
		
		```
			command /chatgpt [<text>]:
				trigger:
					if arg is set:
						send message "&7%player%:&f %arg%" #echo message
						set {_c} to a new conversation message #creates a new conversation message type.
						set {_c}'s gpt content to arg #sets the content to the argument. Role is user by default.
						add {_c} to {gpt::%player%::*} #Store the conversation message into a list. This list will contain all the conversation messages between the player and the AI.
						generate a chat completion with conversation {gpt::%player%::*} #Actually prompt all of the conversation messages to ChatGPT.
						set {_p} to last generated prompt #Retrieve response. It is a conversation message type.
						add {_p} to {gpt::%player%::*} #Save the response as part of the conversation
						send "&7ChatGPT:&f %{_p}%" #Show the response to the player. Converting the response into a string can be done implicitly.
					else:
						send message "&cYou cleared your previous conversation." 
						clear {gpt::%player%::*}
			```
</details>

<details>
	<summary>GPT Completion WITHOUT Conversation Persistence</summary>

	```
	command /chat <text>:
	trigger:
		generate a chat gpt completion with prompt "Who are you?"
		set {_name} to generated prompt
		send {_name}
		#It will say "I am an helpful assistant from OpenAI!"
	```
</details>

### GPT Completion Request

```
(generate|make) [a] [gpt] completion with (prompt|input) %string% [and model %-string%] [and max tokens %-number%] [and temperature %-number%]
(generate|make) [a] [gpt] completion with (prompt|input) %string% [and model %-string%] [and max tokens %-number%] [and temperature %-number%] without echo
```

Generate a completion of an input string using the OpenAI ChatGPT API, based on an input prompt.
ATTENTION! This syntax does not use the ChatGPT endpoint. You won't receive back a conversation from the AI, just a chat completion. It will try to predict what the message is, it won't respond as an assistant. The echo part tells if to include a copy of the input prompt back into the response.

<details>
	<summary>GPT Completion Request</summary>

	```
		command /completion <text>:
	trigger:
		generate a gpt completion with prompt "Who are you?"
		set {_name} to generated prompt
		send {_name}
		#This will not respond with "I am ChatGPT, an helpful assistant made by OpenAI!"
		#It will say instead "I am a human" or "I am Zara" or any other possible endings to that incipit.
	```
</details>


## Types

### Conversation Message
```
%conversationmessage%
%conversationmessages%
conversation message
```

This type holds information about a message in a conversation with ChatGPT. It has two main parts, the role (which can only be set to "assistant", "system" or "user") and the content, which is basically whatever string the user or the AI want to send.

#### Usage
```
send "%conversationmessage%" #implicitly converting it into a string will show its content.
set {_c} to %string% parsed as conversation message #builds a string with default role "user"
generate chat completion with conversation %conversationmessages% #Use conversation persistence.
```

<details>
	<summary>Set bias of conversation</summary>

		```
		command /setbias <text>:
	trigger:
		#This is an example on how to bias your chatgpt conversation.
		#Basically all of your messages will be in the style of the previous one.
		#Pretend the arg is "You're a robot assistant, that talks in a robotic way saying bzz-bzz in between few words. You have a lot of knowledge and usually tell interesting stuff about robots. Your name is HAL"
		set {_bias} to new conversation message with role system #create new conversation message element. Its role is set to system.
		set {_bias}'s gpt content to arg #we set its content to the arg.
		set {gpt::%player%::0} to {_bias} #We append at the beginning of the conversation this element.
		set {gpt::%player%::*} to {gpt::%player%::*} #We fix the indices so we count from 1 
		#from now on, sending a chat completion request with this list will also feed at the beginning a bias about how the AI should behave. Note that resetting the conversation with clear {gpt::%player%::*}  will remove the bias and clear everything.
		```
</details>

<details>
	<summary>Show previous conversation</summary>

		```
		command /showconv:
	trigger:
		#we suppose all the conv is stored in {gpt::%player%::*}. The list is made of conversation messages.
		loop {gpt::%player%::*}:
			if loop-value's gpt role is "user":
				send message "%player%: %loop-value%" #we can also use %gpt content of loop-value%. Basically if the role of the message is user, we are sure it has been sent by the player. If we want to display it to them we send them a message with their name (to make it customized, instead of generic "user") and their message
			else:
				send message "%gpt role of loop-value%: %loop-value%"
		```
</details>

## Expressions

### Generated Prompt
```
[the] [last] generated prompt
```

Returns the last generated response from a Chat Completion request. Call this immediately after sending a new chatgpt prompt, and save it into a variable.

<details>
	<summary>Last Generated Prompt from Chat Completion Request</summary>

		```
		command /chat <text>:
	trigger:
		generate a chat gpt completion with prompt "Who are you?"
		set {_name} to generated prompt
		send {_name}
		```
</details>

### New Conversation Message
```
[a] new conversation message [with role [user|assistant|system]]
```

Returns a conversation message type with a role and an empty content. if the [with role] part of the syntax is not specified, defaults to user.

<details>
	<summary>Create New Conversation Message</summary>

		```
		command /chatcompletion [<text>]:
	trigger:
		if arg is set:
			send message "&7%player%:&f %arg%"
			set {_c} to a new conversation message with role user
			set {_c}'s gpt content to arg
			add {_c} to {gpt::%player%::*}
			generate a chatgpt completion with conversation {gpt::%player%::*}
			set {_p} to last generated prompt
			add {_p} to {gpt::%player%::*}
			send "&7ChatGPT:&f %{_p}%"
		else:
			send message "&cYou cleared your previous conversation."
			clear {gpt::%player%::*}
		```
</details>

### GPT Role of Conversation Message
```
gpt role of %conversationmessage%
%conversationmessage%'s gpt role
```

Returns a string, which indicates the sender of the message. The role can only be either "system", "user" or "assistant".

<details>
	<summary>Set Role of Conversation Message</summary>

		```
		set {_msg} to new conversation message #defaults role to "user"
		set gpt role of {_msg} to "system"
		```
</details>

### GPT Content of Conversation Message
```
gpt content of %conversationmessage%
%conversationmessage%'s gpt content
```

Returns the content of a conversation message.

<details>
	<summary>See another player's conversation</summary>

	```
	command /inspectconv <offlineplayer>:
	trigger:
		if {gpt::%arg%::*} is set:
			send message "&7Conversation of %arg%:"
			loop {gpt::%arg%::*}:
				send message "%gpt role of loop-value%: %gpt content of loop-value%"
	```
</details>


### How to use conversation persistence?

After you red the docs, you might wondering whati is conversation persistence and how to implement it in your skript. Conversation persistence allows the assistant to remember what you told it in the past. It's like remembering your past messages, and it can be more helpful and contextual with your prompts. To implement it, you just have to store all the prompts from a player (and the AI responses) under the form of a Conversation Message in a Skript list. After that, simply feed the list back in the assistant (with the Chat Completion Request expression) and you'll get back a response that is based on the whole conversation, and not only the last prompt. 


## Example usage
```
command /swordgpt:
	trigger:
		generate a chatgpt completion with prompt "Give me the name of an epic sword. In your response only include the name, and color it with minecraft colors. Do not include anything else. JUST THE NAME. JUST THE NAME IN THE RESPONSE. Example: &dAncient &5Sword or &eSword of &cFire"
		set {_name} to generated prompt
		replace all \"%nl%%nl%\" in {_name} with "" #ChatGPT sometimes adds a double newline at the beginning of the completion.
		add 1 diamond sword named {_name} to player's inventory
```




## Setup

- Drag the addon in your plugin folder
- Restart the server
- Open the config in Plugins/SkriptGPT/config.yml
- Type your OpenAI key in 
```
openai_token: "OPEN-AI-KEY"
```
See the FAQ below to learn how to get your API Key.


## FAQ

#### How do I get an API key?

To get an API key, simply create an OpenAI account here:
https://platform.openai.com/signup

After your account is created, you'll be granted 5$ in free trial credits that you will be able to use anywhere. From your OpenAI overview, go to Personal and click on View API keys. From there just create a key and copy it into the config. 

#### What is the difference between the ChatCompletion and Completion effects?

The difference is that the ChatCompletion will always pretend to be an assistant, and will try to respond to you as in a chat. It's basically what ChatGPT is. A prompt like "Who are you" will result in "Hi! I am an helpful assistant from OpenAI...".

A Completion is simply a prompt completion. It will just try to finish whatever text you feed it into. So the "Who are you" input will try to generically complete it, maybe by typing out a conversation between 2 generic people. "I'm a human being."

![Immagine 2023-03-26 105423](https://user-images.githubusercontent.com/61651096/227765440-c2904ed0-de59-4060-8e47-a8d146a72ca3.png)

[OpenAI's documentation](https://platform.openai.com/docs/guides/completion)

#### Do I have to pay for the API to actually use this?

Well, yes. The 5 dollars free trial will last you for about a couple of months if you only use the latest model (gpt-3.5-turbo, enabled by default). This model is really fast and isn't expensive, so it's what I recommend. The completion effect (not chat) might be more expensive and uses text-davinci-003 by default.

You can view OpenAI's article about pricing [here](https://openai.com/pricing)

This addon of course is completely free and uses your API key to make requests.

If you create an assistant service in your server to allow players to "chat" with ChatGPT, remember to add some sort of delay or limit to how much users can use the assistant. Remember that completions are being sent with your api key, so players might consume your credit really quickly.

#### Does the ChatGPT effect remember my previous conversation?

At this moment, every prompt sent to ChatGPT (With the Chat Completion Effect) will be from a new conversation. So saying "I am DereWah" and then "Who Am I?" will result in inconsistent answers. It will be definetely be added in the next updates of the addon.

