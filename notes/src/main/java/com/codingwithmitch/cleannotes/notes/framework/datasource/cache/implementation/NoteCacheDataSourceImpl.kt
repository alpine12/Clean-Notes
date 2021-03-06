package com.codingwithmitch.notes.datasource.cache.repository

import com.codingwithmitch.cleannotes.notes.framework.datasource.mappers.NoteMapper
import com.codingwithmitch.cleannotes.notes.business.data.abstraction.NoteCacheDataSource
import com.codingwithmitch.cleannotes.notes.framework.datasource.model.NoteEntity
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import com.codingwithmitch.cleannotes.core.business.DateUtil
import com.codingwithmitch.notes.datasource.cache.db.NoteDao
import com.codingwithmitch.cleannotes.core.di.scopes.FeatureScope
import com.codingwithmitch.notes.datasource.cache.db.returnOrderedQuery
import javax.inject.Inject

@FeatureScope
class NoteCacheDataSourceImpl
@Inject
constructor(
    private val noteDao: NoteDao,
    private val noteMapper: NoteMapper,
    private val dateUtil: DateUtil
): NoteCacheDataSource {

    override suspend fun insertNewNote(title: String, body: String): Long {
        val note = NoteEntity(
            id = null,
            title = title,
            body = body,
            created_at = dateUtil.convertServerStringDateToLong(dateUtil.getCurrentTimestamp()),
            updated_at = dateUtil.convertServerStringDateToLong(dateUtil.getCurrentTimestamp())
        )
        return noteDao.insertNote(note)
    }

    override suspend fun restoreDeletedNote(
        title: String,
        body: String,
        created_at: String,
        updated_at: String
    ): Long {
        return noteDao.restoreDeletedNote(
            title = title,
            body = body,
            created_at = dateUtil.convertServerStringDateToLong(created_at),
            updated_at = dateUtil.convertServerStringDateToLong(updated_at)
        )
    }

    override suspend fun deleteNote(primaryKey: Int): Int {
        return noteDao.deleteNote(primaryKey)
    }

    override suspend fun updateNote(primaryKey: Int, newTitle: String, newBody: String?): Int {
        return noteDao.updateNote(
            primaryKey = primaryKey,
            title = newTitle,
            body = newBody,
            updated_at = dateUtil.convertServerStringDateToLong(dateUtil.getCurrentTimestamp())
        )
    }

    override suspend fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Note> {
        return noteMapper.entityListToNoteList(
            noteDao.returnOrderedQuery(
                query = query,
                filterAndOrder = filterAndOrder,
                page = page
            )
        )
    }

    override suspend fun getNumNotes() = noteDao.getNumNotes()

    override suspend fun insertNotes(notes: List<Note>): LongArray{
        return noteDao.insertNotes(
            noteMapper.noteListToEntityList(notes)
        )
    }
}






















