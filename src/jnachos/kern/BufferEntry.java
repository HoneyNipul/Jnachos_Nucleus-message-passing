package jnachos.kern;

public class BufferEntry {
int id;
boolean available;
String sender;
String receiver;
String message;
String Answer;
int result;

public String getAnswer() {
	return Answer;
}
public void setAnswer(String answer) {
	Answer = answer;
}
public int getResult() {
	return result;
}
public void setResult(int result) {
	this.result = result;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public boolean isAvailable() {
	return available;
}
public void setAvailable(boolean available) {
	this.available = available;
}
public String getSender() {
	//System.out.println("i getter::"+id + sender);

	return sender;
}
public void setSender(String sender) {
	//System.out.println("i setter::"+id + sender);
	this.sender = sender;
}
public String getReceiver() {
	return receiver;
}
public void setReceiver(String receiver) {
	this.receiver = receiver;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}

}
