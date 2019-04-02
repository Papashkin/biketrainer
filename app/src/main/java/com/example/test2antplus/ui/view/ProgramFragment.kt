package com.example.test2antplus.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.test2antplus.MainApplication
import com.example.test2antplus.MainApplication.Companion.ACTION_WORK_SENDING
import com.example.test2antplus.MainApplication.Companion.ARGS_PROGRAM
import com.example.test2antplus.R
import com.example.test2antplus.data.programs.Program
import com.example.test2antplus.presenter.ProgramPresenter
import com.example.test2antplus.ui.adapter.ProgramAdapter
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
import kotlinx.android.synthetic.main.fragment_programs.*


interface ProgramInterface {
    fun selectProgram()
    fun showLoading()
    fun hideLoading()

    fun setProgramsList(programsList: ArrayList<Program>)

    fun showProgramsList()
    fun hideProgramsList()

    fun updateProgramsList(id: Int)

    fun chooseProgramAndCloseScreen(programName: String)
}

class ProgramFragment : BaseFragment(), ProgramInterface {
    companion object {
        const val WORK = "it's time to work"
        const val PROFILE_NAME = "profile name"
    }

    private lateinit var presenter: ProgramPresenter
    private lateinit var programAdapter: ProgramAdapter

    private var isTime2work: Boolean = false
    private var profileName: String = ""

    //    private var dialog: Dialog? = null
    fun newInstance(isTime2work: Boolean, profile: String): ProgramFragment = ProgramFragment().apply {
        arguments = Bundle().apply {
            putBoolean(WORK, isTime2work)
            putString(PROFILE_NAME, profile)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        MainApplication.graph.inject(this)
        return inflater.inflate(R.layout.fragment_programs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showLoading()
        presenter = ProgramPresenter(this)

        this.arguments?.apply {
            isTime2work = this.getBoolean(WORK)
            profileName = this.getString(PROFILE_NAME, "")
        }

        if (profileName.isNotEmpty()) {
            toolbarPrograms.setTitle(R.string.toolbar_select_program)
        } else {
            toolbarPrograms.setTitle(R.string.toolbar_programs)
        }

        toolbarPrograms.setNavigationIcon(R.drawable.ic_arrow_back_32)
        toolbarPrograms.setNavigationOnClickListener {
            presenter.onBackPressed()
        }

        programAdapter = ProgramAdapter(
            onDeleteClick = {
                AlertDialog.Builder(context!!)
                    .setMessage(resources.getString(R.string.dialog_message_are_you_sure))
                    .setPositiveButton(resources.getString(R.string.dialog_yes)) { dialog, _ ->
                        presenter.onDeleteClick(it)
                        dialog.dismiss()
                    }
                    .setNegativeButton(resources.getString(R.string.dialog_no)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            },
            onEditClick = {
                presenter.onEditClick(it)
            },
            onItemClick = {
                if (isTime2work) {
                    presenter.setWorkOut(it, profileName)
                }
            })
        listPrograms.adapter = programAdapter

        buttonAddProgram.setOnClickListener {
            presenter.addProgram()
        }

    }

    override fun selectProgram() {
    }

    override fun showLoading() {
        pbPrograms.show()
    }

    override fun hideLoading() {
        pbPrograms.hide()
    }

    override fun hideProgramsList() {
        emptyListPrograms.visibility = View.VISIBLE
        listPrograms.visibility = View.GONE
    }

    override fun showProgramsList() {
        emptyListPrograms.visibility = View.GONE
        listPrograms.visibility = View.VISIBLE
    }

    override fun setProgramsList(programsList: ArrayList<Program>) {
        programAdapter.setProgramList(programsList)
        hideLoading()
    }

    override fun updateProgramsList(id: Int) {
        programAdapter.removeItem(id)
    }

    override fun chooseProgramAndCloseScreen(programName: String) {
        activity?.sendBroadcast(Intent(ACTION_WORK_SENDING).apply {
            this.putExtra(ARGS_PROGRAM, programName)
        })
    }
}