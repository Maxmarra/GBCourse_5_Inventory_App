package com.example.inventory


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.inventory.data.*
import com.example.inventory.databinding.FragmentItemDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * [ItemDetailFragment] displays the details of the selected item.
 */
class ItemDetailFragment : Fragment() {

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()

/* TODO
     Создай объект viewModel через InventoryViewModelFactory(
     (activity?.application as InventoryApplication).database.itemDao()) */
    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    lateinit var item: Item


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    /* TODO
        Переопредели onViewCreated() если его тут нет */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    /* TODO
        создай переменную id принимающую значение из переданного
        аргумента из  ItemListFragment
        используй navigationArgs. Имя аргумента находится в nav_graph.xml
        */

        val id = navigationArgs.itemId
    /* TODO
        через viewModel вызови метод получения объекта по id
        и повесь на него обзервер
        полученный объект it присвой lateinit item из класса ItemDetailFragment
        и передай его в метод bind() чтобы заполнить поля xml файла
        данными по этому объекту */
        viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) {
            item = it
            bind(item)
        }
    }

    /* TODO
        создай метод bind() принимающий Item
        его задача связать поля xml файла со значениями переданного объекта
        то есть полю itemName.text нужно присвоить значение из объекта item
        -> item.itemName и так для трех полей + установить
        OnClickListenerы на две кнопки
        используй binding.apply{} чтобы не повторять слово binding 5 раз
        не забудь преобразовывать поля ОБЪЕКТА item в тип String!
        если они имею какой-либо другой формат
        у цены вызывай extention метод - он уже возвращает строку
     */
    private fun bind(item: Item) {
        binding.apply {
            itemName.text = item.itemName
            itemPrice.text = item.getFormattedPrice()
            itemCount.text = item.quantityInStock.toString()

            // присвой кнопке sellItem значение isEnabled
            // как результат работы метода isStockAvailable()
            sellItem.isEnabled = viewModel.isStockAvailable(item)

            //установи на кнопку sellItem setOnClickListener и передай в него
            //метод продажи единицы товара sellItem()
            sellItem.setOnClickListener { viewModel.sellItem(item) }

            //установи на кнопку deleteItem setOnClickListener
            //и передай в него метод вывода диалогового окна
            // showConfirmationDialog()
            deleteItem.setOnClickListener { showConfirmationDialog() }
        }
    }

    /* TODO
        создай метод showConfirmationDialog()
        его задача показать окно с просьбой подтвердить или
        отменить удаления item
        при подтверждении -> применить метод deleteItem()
     */
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteItem()
            }
            .show()
    }
    /*TODO
        создай метод deleteItem()
        его задача вызвать соответствующий метод у viewModel
        который удалит item из базы и после это
        сделает навигацию, вернувшись назад к списку items
        через метод findNavController().navigateUp()
    * */
    private fun deleteItem() {
        viewModel.deleteItem(item)
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
