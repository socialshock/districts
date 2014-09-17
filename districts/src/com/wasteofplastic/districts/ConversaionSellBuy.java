package com.wasteofplastic.districts;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

public class ConversaionSellBuy implements Prompt {
    public enum Type { RENT, SELL };

    private Districts plugin;
    private Type type;

    /**
     * @param plugin
     */
    public ConversaionSellBuy(Districts plugin, Type type) {
	this.plugin = plugin;
	this.type = type;
    }

    @Override
    public String getPromptText(ConversationContext context) {
	switch (type) {
	case RENT:
	    return ChatColor.AQUA + "Enter the rent amount";
	case SELL:
	    return ChatColor.AQUA + "Enter the district price";
	default:
	}
	return ChatColor.AQUA + "Enter the amount";
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
	return true;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
	if (input.isEmpty()) {
	    return END_OF_CONVERSATION;
	}
	DistrictRegion d = (DistrictRegion) context.getSessionData("District");
	double price = 0D;
	if (d != null) {
	    try {
		price = Double.valueOf(input);
		if (price <= 1D) {
		    context.getForWhom().sendRawMessage(ChatColor.RED + "Amount must be more than " + VaultHelper.econ.format(1D));
		    return this;
		}
	    } catch (Exception e) {
		context.getForWhom().sendRawMessage(ChatColor.RED + "How much?");
		return this;
	    }
	    switch (type) {
	    case RENT:
		context.getForWhom().sendRawMessage(ChatColor.GOLD + "Putting district up for rent for " + VaultHelper.econ.format(price));
		d.setForRent(true);
		d.setForSale(false);
		d.setPrice(price);
		break;
	    case SELL:
		context.getForWhom().sendRawMessage(ChatColor.GOLD + "Putting district up for sale for " + VaultHelper.econ.format(price));
		d.setForSale(true);
		d.setPrice(price);
		d.setForRent(false);
		break;
	    default:
		break;
	    }
	}
	return END_OF_CONVERSATION;
    }
}
