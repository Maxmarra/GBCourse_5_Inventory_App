package com.example.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventory.data.InventoryApplication
import com.example.inventory.data.InventoryViewModel
import com.example.inventory.data.InventoryViewModelFactory
import com.example.inventory.databinding.FragmentItemListBinding

class ItemListFragment : Fragment() {

    /* TODO
         Создай объект viewModel через InventoryViewModelFactory(
         (activity?.application as InventoryApplication).database.itemDao()) */
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
        /* TODO
            Переопредели onViewCreated() если его тут нет */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*TODO
            создай объект ItemListAdapterа - adapter
            в {} опиши работу при нажатии на элемент списка
            создай переменную action и через ItemListFragmentDirections
            навигацию передай id этого элемента списка
            который потом будет принят в ItemDetailFragment и подставлен
            в метод вызова из базы элемента по его id [retrieveItem(id) ->
            itemDao.getItemDao(id)]
            соверши навигацию через this.findNavController()... */
        val adapter = ItemListAdapter {
            val action = ItemListFragmentDirections
                .actionItemListFragmentToItemDetailFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
        /*TODO
           отследи изменения в списке если вдруг в него добавили или удалили
           какой-то объект
           повесь слушатель на allItems
           назначь ЭТОТ фрагмент viewLifecycleOwner
           вызови у всех элементов метод let{} для работы
           только с ненулабельными значениями
           и у ОБЪЕКТА  ItemListAdapter вызови метод submitList()
           передав в него весь список через it
           submitList() это метод идущий в связке с ListAdapter
           он проверяет и после обновляет ТОЛЬКО изменные части списка
           а не переписывает заново весь список */
        viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }

        /* TODO
            установи для recyclerView.layoutManager -> LinearLayoutManager(this.context) */
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)

        /* TODO
            повесь на FAB OnClickListener
            создай переменную action через ItemListFragmentDirections
            куда передай принимаемый в nav_graph.xml параметр title
            через getString(R.string.add_fragment_title)
            Так как у нас ОДИН экран и для добавления и для редактирования элемента
            то при переходе на этот экран для ДОБАВЛЕНИЯ элемента, мы подставим этому
            экрану заголовок Add Item, а при редактировании будет заголовок Edit Item
            после этого соверши навигацию this.findNavController()...*/
        binding.floatingActionButton.setOnClickListener {
            val action = ItemListFragmentDirections.actionItemListFragmentToAddItemFragment(
                getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)
        }
    }
}
