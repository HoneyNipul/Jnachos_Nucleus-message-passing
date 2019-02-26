package jnachos.kern;

public class ReadBuffer   {

	
	public void read() {
		
		
		for(int i=0;i<JNachos.bufferPool.length;i++)
		{
			if(JNachos.bufferPool[i].isAvailable()==false)
			{
				System.out.println(" Read buffer called..");
				String receiverProcess = JNachos.bufferPool[i].getReceiver();
				NachosProcess receiverProcessObj = JNachos.processPool.get(receiverProcess);
				//receiverProcessObj.getmQueue().add(JNachos.bufferPool[i].id);
				
				
			}
		}
		
	}
	

}
