package io.rik72.brew.engine.db.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.rik72.brew.engine.db.repositories.TextRepository;
import io.rik72.brew.engine.utils.TextUtils;
import io.rik72.mammoth.entities.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class TextGroup implements AbstractEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private short id;

    @Column
    private String name;

	@OneToMany(mappedBy = "group")
	private List<Text> texts = new ArrayList<>();

	public TextGroup() {
	}

	public TextGroup(String name) {
		this.name = name;
	}

	@Override
	public short getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Text> getTexts() {
		if (texts.isEmpty())
			texts = TextRepository.get().findByGroup(this);
		return texts;
	}

	public List<String> getTextStrings() {
		return getTexts().stream()
			.map(item -> item.getText().strip())
			.collect(Collectors.toList());
	}

    @Override
    public String toString() {
        return "{ TextGroup :: " + 
			"id=" + id + ", " + 
			"name=" + TextUtils.denormalize(name) +
		" }";
    }
}
