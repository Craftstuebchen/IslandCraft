package com.github.hoqhuuep.islandcraft.bukkit.ebeanserver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "death_point")
public class DeathPointBean {
	@Id
	private String player;

	@Column
	private String world;

	@Column
	private Integer x;

	@Column
	private Integer z;

	public String getPlayer() {
		return player;
	}

	public String getWorld() {
		return world;
	}

	public Integer getX() {
		return x;
	}

	public Integer getZ() {
		return z;
	}

	public void setPlayer(final String player) {
		this.player = player;
	}

	public void setWorld(final String world) {
		this.world = world;
	}

	public void setX(final Integer x) {
		this.x = x;
	}

	public void setZ(final Integer z) {
		this.z = z;
	}
}