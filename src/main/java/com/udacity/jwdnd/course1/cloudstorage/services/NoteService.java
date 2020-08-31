package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.forms.NoteForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public Note getNote(String noteTitle) {
        return noteMapper.selectNote(noteTitle);
    }

    public List<Note> getNotesFromUser(Integer userId) {
        return noteMapper.selectNotesFromUser(userId);
    }

    public List<Note> getAllNotes() {
        return noteMapper.selectAllNotes();
    }

    public Integer insertNote(NoteForm noteForm) {
        Note note = new Note();
        note.setNoteTitle(noteForm.getTitle());
        note.setNoteDescription(noteForm.getDescription());
        note.setUserId(noteForm.getUserId());
        return noteMapper.insertNote(note);
    }

    public Integer updateNote(Note note, NoteForm noteForm) {
        note.setNoteTitle(noteForm.getTitle());
        note.setNoteDescription(noteForm.getDescription());
        note.setUserId(noteForm.getUserId());
        return noteMapper.updateNote(note);
    }

    public Integer deleteNote(Note note) {
        Integer noteId = note.getNoteId();
        return noteMapper.deleteNote(noteId);
    }
}