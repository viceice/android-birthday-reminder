package info.kriese.birthdayreminder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import info.kriese.birthdayreminder.fragments.ContactsFragment


const val PERMISSION_REQUEST_READ_CONTACTS = 79;

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            loadContacts();
        } else {
            requestPermission();
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_READ_CONTACTS) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(
                    getView(),
                    R.string.contacts_permission_granted,
                    Snackbar.LENGTH_SHORT
                ).show();
                loadContacts()
            } else {
                // Permission request was denied.
                Snackbar.make(getView(), R.string.contacts_permission_denied, Snackbar.LENGTH_SHORT)
            }
        }
    }

    private fun requestPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            Snackbar.make(
                getView(),
                R.string.contact_access_required,
                Snackbar.LENGTH_INDEFINITE
            ).setAction(R.string.ok) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    PERMISSION_REQUEST_READ_CONTACTS
                )
            }.show()

        } else {
            Snackbar.make(
                getView(),
                R.string.contact_permission_not_available,
                Snackbar.LENGTH_SHORT
            ).show();

            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSION_REQUEST_READ_CONTACTS
            );
        }
    }

    /**
     * Return the instance of View
     *
     * @return CoordinatorLayout
     */
    private fun getView(): View {
        return findViewById(android.R.id.content)
    }

    private fun loadContacts() {
        supportFragmentManager.beginTransaction()
            .add(R.id.container, ContactsFragment())
            .commit()
    }
}
