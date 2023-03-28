
# Skript-GPT

A Skript Addon that allows users to interface with the OpenAI API and lets them send completion requests.

[Documentation](https://skripthub.net/docs/?addon=)


## Usage

```
command /swordgpt:
	trigger:
		generate a chatgpt completion with prompt "Give me the name of an epic sword. In your response only include the name, and color it with minecraft colors. Do not include anything else. JUST THE NAME. JUST THE NAME IN THE RESPONSE. Example: &dAncient &5Sword or &eSword of &cFire"
		set {_name} to generated prompt
		replace all \"%nl%%nl%\" in {_name} with "" #ChatGPT sometimes adds a double newline at the beginning of the completion.
		add 1 diamond sword named {_name} to player's inventory
```

[![SkriptHubViewTheDocs](http://skripthub.net/static/addon/ViewTheDocsButton.png)](http://skripthub.net/docs/?addon=Skript-GPT)


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

