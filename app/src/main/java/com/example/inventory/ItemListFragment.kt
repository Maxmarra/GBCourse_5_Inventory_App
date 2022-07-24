package com.example.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventory.adapter.ItemListAdapter
import com.example.inventory.data.InventoryApplication
import com.example.inventory.data.InventoryViewModel
import com.example.inventory.data.InventoryViewModelFactory
import com.example.inventory.databinding.FragmentItemListBinding


/**
 * Main fragment displaying details for all items in the database.
 */
class ItemListFragment : Fragment() {

    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            itemListFragment = this@ItemListFragment
            recyclerView.adapter = ItemListAdapter{
                val action =
                    ItemListFragmentDirections
                        .actionItemListFragmentToItemDetailFragment(it.id)
                findNavController().navigate(action)
            }
        }
    }

    fun onFabClick(){
        val action = ItemListFragmentDirections
            .actionItemListFragmentToAddItemFragment(
                getString(R.string.add_fragment_title)
            )
        this.findNavController().navigate(action)
    }
}
