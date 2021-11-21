package com.klapeks.colinker;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class ColinConsoleSender implements ConsoleCommandSender {

	ConsoleCommandSender def;
	String message = "";
	public ColinConsoleSender() {
		def = Bukkit.getConsoleSender();
	}
	public String getResponse() {
		if (message.equals("")) {
			return "§c[Colinker] §6This command didn't send any message to you";
		}
		message = ChatColor.stripColor(message);
		return message.startsWith("\n") ? message.replaceFirst("\n", "") : message;
	}
	

	@Override public void sendMessage(String paramString) {
		message += "\n"+paramString;
	}

	@Override public void sendMessage(String[] paramArrayOfString) {
		for (String msg : paramArrayOfString) {
			message += "\n"+msg;
		}
	}

	@Override public Server getServer() {
		return def.getServer();
	}

	@Override public String getName() {
		return "Colinker";
	}

	@Override public boolean isPermissionSet(String paramString) {
		return def.isPermissionSet(paramString);
	}

	@Override public boolean isPermissionSet(Permission paramPermission) {
		return def.isPermissionSet(paramPermission);
	}

	@Override public boolean hasPermission(String paramString) {
		return def.hasPermission(paramString);
	}

	@Override public boolean hasPermission(Permission paramPermission) {
		return def.hasPermission(paramPermission);
	}

	@Override public PermissionAttachment addAttachment(Plugin paramPlugin, String paramString, boolean paramBoolean) {
		return def.addAttachment(paramPlugin, paramString, paramBoolean);
	}

	@Override public PermissionAttachment addAttachment(Plugin paramPlugin) {
		return def.addAttachment(paramPlugin);
	}

	@Override public PermissionAttachment addAttachment(Plugin paramPlugin, String paramString, boolean paramBoolean,
			int paramInt) {
		return def.addAttachment(paramPlugin, paramString, paramBoolean, paramInt);
	}

	@Override public PermissionAttachment addAttachment(Plugin paramPlugin, int paramInt) {
		return def.addAttachment(paramPlugin, paramInt);
	}

	@Override public void removeAttachment(PermissionAttachment paramPermissionAttachment) {
		def.removeAttachment(paramPermissionAttachment);
	}

	@Override public void recalculatePermissions() {
		def.recalculatePermissions();
	}

	@Override public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return def.getEffectivePermissions();
	}

	@Override public boolean isOp() {
		return def.isOp();
	}

	@Override public void setOp(boolean paramBoolean) {
		def.setOp(paramBoolean);
	}

	@Override public boolean isConversing() {
		return def.isConversing();
	}

	@Override public void acceptConversationInput(String paramString) {
		def.acceptConversationInput(paramString);
	}

	@Override public boolean beginConversation(Conversation paramConversation) {
		return def.beginConversation(paramConversation);
	}

	@Override public void abandonConversation(Conversation paramConversation) {
		def.abandonConversation(paramConversation);
	}

	@Override public void abandonConversation(Conversation paramConversation,
			ConversationAbandonedEvent paramConversationAbandonedEvent) {
		def.abandonConversation(paramConversation, paramConversationAbandonedEvent);
	}

	@Override public void sendRawMessage(String paramString) {
		message += "\n"+paramString;
	}

}
