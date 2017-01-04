package serobot.bluetooth.tests;

import org.junit.Test;
import serobot.bluetooth.Command;

import java.util.Random;

import org.junit.Assert;

public class CommandTest {
	@Test
	public void testCommandName() {
		Command command = new Command("blarg");
		Command command2 = Command.fromString(command.toString());
		Assert.assertEquals(command2.getCommandName(), "blarg");
	}
	
	@Test
	public void testEmptyCommandName() {
		Command command = new Command(""); 
		Command command2 = Command.fromString(command.toString());
		Assert.assertEquals(command2.getCommandName(), "");
	}
	
	@Test
	public void testSingleParameter() {
		{// long
			Command command = new Command("test");
			command.setLong("long", 123456789012L);
			Command command2 = Command.fromString(command.toString());
			Assert.assertEquals(command2.getLong("long"), 123456789012L);
		}
		{ // double
			Command command = new Command("test");
			command.setDouble("double", Math.PI);
			Command command2 = Command.fromString(command.toString());
			Assert.assertEquals(command2.getDouble("double"), Math.PI, Double.MIN_NORMAL);
		}
		{ // string
			Command command = new Command("test");
			command.setString("string", "Hello, world!\r\nTest.");
			Command command2 = Command.fromString(command.toString());
			Assert.assertEquals(command2.getString("string"),
					"Hello, world!\r\nTest.");
		}
	}
	
	@Test
	public void testNullStringParameter() {
		Command command = new Command("oh no");
		command.setString("nullString", null);
		Command command2 = Command.fromString(command.toString());
		Assert.assertEquals(null,
				command2.getString("nullString"));
	}
	
	@Test
	public void testManyParameters() {
		Command command = new Command("quiteBig");
		
		Random random = new Random(10);
		for(int i = 0; i < 100; i++) {
			command.setInteger("int" + i, random.nextInt());
			command.setString("string" + i,
					String.valueOf(String.valueOf(random.nextInt()).hashCode()));
		}
		
		Command command2 = Command.fromString(command.toString());
		random = new Random(10);
		for(int i = 0; i < 100; i++) {
			Assert.assertEquals(
					random.nextInt(),
					command2.getInteger("int" + i));
			Assert.assertEquals(String.valueOf(String.valueOf(random.nextInt()).hashCode()),
					command2.getString("string" + i)
					);
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testParameterType() {
		Command command = new Command("shouldntWork");
		
		command.setString("brokeIt", "oops");
		Assert.assertEquals(Command.CommandParameterType.STRING,
				command.getParameterType("brokeIt"));
		command.getFloat("brokeIt");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNonexistentParameter() {
		Command command = new Command("nothingInHere");
		
		command.getBoolean("spooky");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testParameterModification() {
		Command command = new Command("blarg");
		command.setString("param", "value");
		command.setBoolean("flag", false);
		
		Command command2 = Command.fromString(command.toString());
		Assert.assertTrue(command2.hasParameter("param"));
		Assert.assertEquals("value", command2.getString("param"));
		Assert.assertEquals(false, command2.getBoolean("flag"));
		
		command2.setBoolean("flag", true); // shouldn't work
	}
}
