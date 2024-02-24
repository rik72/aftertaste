package io.rik72.brew.engine.db.entities;

import io.rik72.brew.engine.utils.TextUtils;
import io.rik72.mammoth.entities.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Text implements AbstractEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private short id;

	@ManyToOne
    @JoinColumn(name="group_id", nullable=false)
	private TextGroup group;

    @Column
    private int position;

    @Column(length=1024)
    private String text;

	public Text() {
	}

	public Text(TextGroup group, int position, String text) {
		this.group = group;
		this.position = position;
		this.text = text;
	}

	@Override
	public short getId() {
		return id;
	}

	public TextGroup getGroup() {
		return group;
	}

	public int getPosition() {
		return position;
	}

	public String getText() {
		return text;
	}

    @Override
    public String toString() {
        return "{ Text :: " + 
			"id=" + id + ", " + 
			"group=" + TextUtils.denormalize(group.getName()) + ", " + 
			"position=" + position + ", " +
			"text=" + TextUtils.denormalize(text) +
		" }";
    }
}
