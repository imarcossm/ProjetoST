const API_URL = 'http://localhost:8080';

function showSection(name) {
    document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
    document.querySelectorAll('nav button').forEach(b => b.classList.remove('active'));

    const section = document.getElementById(name);
    if (section) {
        section.classList.add('active');
    }

    const btn = Array.from(document.querySelectorAll('nav button')).find(b => b.getAttribute('onclick').includes(name));
    if (btn) {
        btn.classList.add('active');
    }
}

function showMsg(containerId, text, type) {
    const el = document.getElementById(containerId);
    if (el) {
        el.innerHTML = `<div class="msg ${type}">${text}</div>`;
        setTimeout(() => { el.innerHTML = ''; }, 5000);
    }
}

async function apiCall(method, endpoint, body = null) {
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json'
        }
    };
    if (body) {
        options.body = JSON.stringify(body);
    }

    try {
        const response = await fetch(`${API_URL}${endpoint}`, options);
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({ mensagem: 'Erro na requisição' }));
            throw new Error(errorData.mensagem || 'Erro desconhecido');
        }
        if (response.status === 204) return null;
        return await response.json();
    } catch (error) {
        console.error('Erro na API:', error);
        throw error;
    }
}

async function salvarCliente() {
    const nome = document.getElementById('c-nome').value;
    const email = document.getElementById('c-email').value;
    const data = document.getElementById('c-data').value;

    if (!nome || !email || !data) {
        showMsg('cliente-msg', 'Preencha todos os campos.', 'error');
        return;
    }

    try {
        await apiCall('POST', '/clientes', { nome, email, dataCadastro: data });
        showMsg('cliente-msg', 'Cliente cadastrado com sucesso!', 'success');
        document.getElementById('c-nome').value = '';
        document.getElementById('c-email').value = '';
        document.getElementById('c-data').value = '';
        listarClientes();
    } catch (e) {
        showMsg('cliente-msg', e.message, 'error');
    }
}

async function listarClientes() {
    try {
        const data = await apiCall('GET', '/clientes?size=100');
        renderTabelaClientes(data.content || data);
    } catch (e) {
        console.error(e);
    }
}

async function buscarClientes() {
    const nome = document.getElementById('c-busca').value;
    if (!nome) {
        listarClientes();
        return;
    }
    try {
        const data = await apiCall('GET', `/clientes/buscar?nome=${encodeURIComponent(nome)}`);
        renderTabelaClientes(data.content || data);
    } catch (e) {
        showMsg('cliente-busca-msg', e.message, 'error');
    }
}

function renderTabelaClientes(lista) {
    const container = document.getElementById('clientes-result');
    if (!lista || lista.length === 0) {
        container.innerHTML = '<p>Nenhum cliente encontrado.</p>';
        return;
    }

    let html = `
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>E-mail</th>
                    <th>Data Cadastro</th>
                </tr>
            </thead>
            <tbody>
    `;

    lista.forEach(c => {
        html += `
            <tr>
                <td>${c.id}</td>
                <td>${c.nome}</td>
                <td>${c.email}</td>
                <td>${c.dataCadastro}</td>
            </tr>
        `;
    });

    html += '</tbody></table>';
    container.innerHTML = html;
}

async function salvarProduto() {
    const nome = document.getElementById('p-nome').value;
    const valor = document.getElementById('p-valor').value;
    const estoque = document.getElementById('p-estoque').value;
    const data = document.getElementById('p-data').value;

    if (!nome || !valor || !estoque || !data) {
        showMsg('produto-msg', 'Preencha todos os campos.', 'error');
        return;
    }

    try {
        await apiCall('POST', '/produtos', {
            nome,
            valor: parseFloat(valor),
            estoque: parseInt(estoque),
            dataCadastro: data
        });
        showMsg('produto-msg', 'Produto cadastrado com sucesso!', 'success');
        document.getElementById('p-nome').value = '';
        document.getElementById('p-valor').value = '';
        document.getElementById('p-estoque').value = '';
        document.getElementById('p-data').value = '';
        listarProdutos();
    } catch (e) {
        showMsg('produto-msg', e.message, 'error');
    }
}

async function listarProdutos() {
    try {
        const data = await apiCall('GET', '/produtos?size=100');
        renderTabelaProdutos(data.content || data);
    } catch (e) {
        console.error(e);
    }
}

async function buscarProdutos() {
    const nome = document.getElementById('p-busca').value;
    if (!nome) {
        listarProdutos();
        return;
    }
    try {
        const data = await apiCall('GET', `/produtos/buscar?nome=${encodeURIComponent(nome)}`);
        renderTabelaProdutos(data.content || data);
    } catch (e) {
        showMsg('produto-busca-msg', e.message, 'error');
    }
}

function renderTabelaProdutos(lista) {
    const container = document.getElementById('produtos-result');
    if (!lista || lista.length === 0) {
        container.innerHTML = '<p>Nenhum produto encontrado.</p>';
        return;
    }

    let html = `
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Descrição</th>
                    <th>Valor</th>
                    <th>Estoque</th>
                </tr>
            </thead>
            <tbody>
    `;

    lista.forEach(p => {
        html += `
            <tr>
                <td>${p.id}</td>
                <td>${p.nome}</td>
                <td>R$ ${p.valor.toFixed(2)}</td>
                <td>${p.estoque}</td>
            </tr>
        `;
    });

    html += '</tbody></table>';
    container.innerHTML = html;
}

let itemCount = 0;

function addItem() {
    itemCount++;
    const container = document.getElementById('items-container');
    const div = document.createElement('div');
    div.className = 'item-row';
    div.id = `item-row-${itemCount}`;
    div.innerHTML = `
        <div>
            <label>ID Produto</label>
            <input type="number" id="item-prod-${itemCount}" onblur="autoPreencherPreco(this)" required>
        </div>
        <div>
            <label>Qtd</label>
            <input type="number" id="item-qtd-${itemCount}" value="1" min="1" required>
        </div>
        <div>
            <label>Preço Unit.</label>
            <input type="number" id="item-val-${itemCount}" step="0.01" required>
        </div>
        <div>
            <label>Desc.</label>
            <input type="number" id="item-desc-${itemCount}" step="0.01" value="0">
        </div>
        <button type="button" class="btn btn-danger" onclick="removeItem(${itemCount})">Remover</button>
    `;
    container.appendChild(div);
}

function removeItem(id) {
    const row = document.getElementById(`item-row-${id}`);
    if (row) row.remove();
}

async function autoPreencherPreco(input) {
    const id = input.value;
    const index = input.id.split('-').pop();
    const valInput = document.getElementById(`item-val-${index}`);

    if (!id) return;

    try {
        const p = await apiCall('GET', `/produtos/${id}`);
        if (p && p.valor) {
            valInput.value = p.valor.toFixed(2);
        }
    } catch (e) {
        console.warn('Não foi possível buscar o preço do produto:', e.message);
    }
}

async function criarPedido() {
    const clienteId = document.getElementById('ped-cliente').value;
    const dataPedido = document.getElementById('ped-data').value;

    if (!clienteId || !dataPedido) {
        showMsg('pedido-msg', 'Preencha cliente e data.', 'error');
        return;
    }

    const itens = [];
    const itemRows = document.querySelectorAll('.item-row');
    if (itemRows.length === 0) {
        showMsg('pedido-msg', 'Adicione pelo menos um produto.', 'error');
        return;
    }

    itemRows.forEach(row => {
        const id = row.id.split('-').pop();
        itens.push({
            produtoId: parseInt(document.getElementById(`item-prod-${id}`).value),
            quantidade: parseInt(document.getElementById(`item-qtd-${id}`).value),
            valor: parseFloat(document.getElementById(`item-val-${id}`).value),
            desconto: parseFloat(document.getElementById(`item-desc-${id}`).value) || 0
        });
    });

    try {
        const res = await apiCall('POST', '/pedidos', { clienteId: parseInt(clienteId), dataPedido, itens });
        showMsg('pedido-msg', `Pedido #${res.id} criado com sucesso!`, 'success');
        document.getElementById('ped-cliente').value = '';
        document.getElementById('ped-data').value = '';
        document.getElementById('items-container').innerHTML = '';
        itemCount = 0;
    } catch (e) {
        showMsg('pedido-msg', e.message, 'error');
    }
}

async function listarTodosPedidos() {
    try {
        const data = await apiCall('GET', '/pedidos?size=100');
        renderTabelaPedidos(data.content || data, 'pedidos-result-list');
    } catch (e) {
        console.error(e);
    }
}

async function buscarPedidoPorId() {
    const id = document.getElementById('ped-id-busca').value;
    if (!id) return;
    try {
        const data = await apiCall('GET', `/pedidos/${id}`);
        renderTabelaPedidos([data], 'pedidos-result-list');
    } catch (e) {
        showMsg('pedido-busca-msg', e.message, 'error');
    }
}

async function buscarPedidosPorPeriodoUI() {
    const inicio = document.getElementById('ped-data-inicio').value;
    const fim = document.getElementById('ped-data-fim').value;
    if (!inicio || !fim) {
        showMsg('pedido-busca-msg', 'Informe as datas inicial e final.', 'error');
        return;
    }
    try {
        const data = await apiCall('GET', `/pedidos/periodo?dataInicio=${inicio}&dataFim=${fim}`);
        renderTabelaPedidos(data.content || data, 'pedidos-result-list');
    } catch (e) {
        showMsg('pedido-busca-msg', e.message, 'error');
    }
}

function renderTabelaPedidos(lista, containerId) {
    const container = document.getElementById(containerId);
    if (!lista || lista.length === 0 || (lista.length === 1 && !lista[0].id)) {
        container.innerHTML = '<p>Nenhum pedido encontrado.</p>';
        return;
    }

    let html = `
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Cliente</th>
                    <th>Data</th>
                    <th>Desconto</th>
                    <th>Total</th>
                    <th>Produtos</th>
                </tr>
            </thead>
            <tbody>
    `;

    lista.forEach(p => {
        const totalDesconto = (p.itens || []).reduce((acc, i) => acc + (i.desconto || 0), 0);
        const itensStr = (p.itens || []).map(i => `${i.produtoId} - ${i.produtoNome}: ${i.quantidade}`).join(', ');
        html += `
            <tr>
                <td>${p.id}</td>
                <td>${p.clienteNome}</td>
                <td>${p.dataPedido}</td>
                <td>R$ ${totalDesconto.toFixed(2)}</td>
                <td>R$ ${p.total.toFixed(2)}</td>
                <td style="font-size: 0.85rem; color: #444;">${itensStr}</td>
            </tr>
        `;
    });

    html += '</tbody></table>';
    container.innerHTML = html;
}

async function pedidosPorCliente() {
    const id = document.getElementById('q-cliente-id').value;
    if (!id) return;
    try {
        const data = await apiCall('GET', `/pedidos/cliente/${id}`);
        renderTabelaPedidos(data.content || data, 'q-result');
    } catch (e) {
        showMsg('q-msg', e.message, 'error');
    }
}

async function totalGastoPorCliente() {
    const id = document.getElementById('q-cliente-id').value;
    if (!id) return;
    try {
        const total = await apiCall('GET', `/pedidos/cliente/${id}/total`);
        document.getElementById('q-result').innerHTML = `<div class="card"><h3>Total Gasto: R$ ${parseFloat(total).toFixed(2)}</h3></div>`;
    } catch (e) {
        showMsg('q-msg', e.message, 'error');
    }
}

async function pedidosPorProduto() {
    const id = document.getElementById('q-produto-id').value;
    if (!id) return;
    try {
        const data = await apiCall('GET', `/pedidos/produto/${id}`);
        renderTabelaPedidos(data.content || data, 'q-result');
    } catch (e) {
        showMsg('q-msg', e.message, 'error');
    }
}

async function pedidosPorPeriodo() {
    const inicio = document.getElementById('q-data-inicio').value;
    const fim = document.getElementById('q-data-fim').value;
    if (!inicio || !fim) return;
    try {
        const data = await apiCall('GET', `/pedidos/periodo?dataInicio=${inicio}&dataFim=${fim}`);
        renderTabelaPedidos(data.content || data, 'q-result');
    } catch (e) {
        showMsg('q-msg', e.message, 'error');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    listarClientes();
    listarProdutos();
});
