package info.kriese.birthdayreminder.fragments

import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.kriese.birthdayreminder.R
import info.kriese.birthdayreminder.adapters.ContactsCursorAdapter

val PROJECTION: Array<out String> = arrayOf(
    ContactsContract.Contacts._ID,
    ContactsContract.Contacts.LOOKUP_KEY,
    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
    ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
    ContactsContract.CommonDataKinds.Event.START_DATE
)


class ContactsFragment :
    Fragment(),
    LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var contactsList: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var cursorAdapter: ContactsCursorAdapter


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val where =
            "${ContactsContract.Data.MIMETYPE} = ? AND ${ContactsContract.CommonDataKinds.Event.TYPE} = ${ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY}"

        val whereArgs = arrayOf(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
        return activity?.let {
            return CursorLoader(
                it,
                ContactsContract.Data.CONTENT_URI,
                PROJECTION,
                where,
                whereArgs,
                "${ContactsContract.CommonDataKinds.Event.START_DATE} ASC"
            )
        } ?: throw IllegalStateException()
    }


    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        cursorAdapter.swapCursor(cursor)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        cursorAdapter.swapCursor(null)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contacts_list_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        LoaderManager.getInstance(this).initLoader(0, null, this)


        activity?.also {
            viewManager = LinearLayoutManager(it)
            cursorAdapter = ContactsCursorAdapter();

            contactsList = it.findViewById<RecyclerView>(R.id.recyclerview).apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = cursorAdapter
            }
        }
    }
}